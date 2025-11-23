package test.task.impl.repositories

import android.content.Context
import android.net.Uri
import androidx.annotation.FloatRange
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import test.task.auth.AuthState
import test.task.auth.repositories.AuthRepository
import test.task.books.Book
import test.task.books.BookFormat
import test.task.books.repositories.BooksRepository
import test.task.books.repositories.DownloadProgress
import test.task.datasources.BooksLocalDataSource
import test.task.datasources.BooksLocalMetadataDataSource
import test.task.datasources.BooksRemoteDataSource
import test.task.datasources.BooksRemoteMetadataDataSource
import java.io.File
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val localFilesDs: BooksLocalDataSource,
    private val localMetaDs: BooksLocalMetadataDataSource,
    private val remoteMetaDs: BooksRemoteMetadataDataSource,
    private val remoteFileDs: BooksRemoteDataSource,
    private val authRepository: AuthRepository,
) : BooksRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBooksAsFlow(): Flow<List<Book>> {
        return authRepository.getAuthStateAsFlow()
            .flatMapLatest { authState ->
                when (authState) {
                    is AuthState.Success -> {
                        val uid = authState.user.uid

                        combine(
                            remoteMetaDs.getBooksAsFlow(uid),
                            localMetaDs.getBooksAsFlow()
                        ) { remote, local ->
                            val localErrors = local.filter {
                                it.localPath == null || localFilesDs.getFile(it.localPath!!) == null
                            }
                            if (localErrors.isNotEmpty()) localMetaDs.deleteBooks(localErrors.map { it.id })
                            val localExisting = local.filter {
                                it.localPath != null &&
                                        localFilesDs.getFile(it.localPath!!) != null
                            }

                            val merged = mutableListOf<Book>().apply {
                                addAll(localExisting)

                                remote.forEach { r ->
                                    if (none { it.id == r.id }) {
                                        add(r)
                                    }
                                }
                            }

                            merged
                        }
                    }

                    else -> {
                        localMetaDs.getBooksAsFlow().map { local ->
                            val localErrors = local.filter {
                                it.localPath == null || localFilesDs.getFile(it.localPath!!) == null
                            }
                            if (localErrors.isNotEmpty()) localMetaDs.deleteBooks(localErrors.map { it.id })
                            val localExisting = local.filter {
                                it.localPath != null &&
                                        localFilesDs.getFile(it.localPath!!) != null
                            }
                            localExisting
                        }
                    }
                }
            }
    }

    override suspend fun uploadBook(
        title: String,
        author: String?,
        fileUri: Uri
    ): Book = withContext(ioDispatcher) {
        val mime = appContext.contentResolver.getType(fileUri)
        val format = mime?.let { BookFormat.fromMimeType(it) } ?: BookFormat.TXT
        val ext = BookFormat.toExt(format)

        val fileName = "${title}_${author ?: "Неизвестный автор"}.$ext"

        val uid = when (val auth = authRepository.getAuthStateAsFlow().first()) {
            is AuthState.Success -> auth.user.uid
            else -> throw IllegalStateException("User is not authenticated")
        }

        val savedFile = appContext.contentResolver.openInputStream(fileUri)?.use {
            localFilesDs.saveFile(it, fileName)
        } ?: throw IllegalStateException("Failed to read file Uri")

        val uploadResult = remoteFileDs.uploadFile(
            fileUri = savedFile.toUri(),
            userId = uid,
            fileName = title
        )

        val book = Book(
            id = "",
            title = title,
            author = author,
            remoteUrl = uploadResult.url,
            localPath = null,
            format = BookFormat.fromMimeType(uploadResult.contentType) ?: BookFormat.TXT,
            readProgress = 0f
        )

        val bookId = remoteMetaDs.addBook(book, uid)

        val actualBook = book.copy(
            id = bookId,
            localPath = savedFile.path
        )
        localMetaDs.addBook(actualBook)

        actualBook
    }


    override suspend fun downloadBook(book: Book): Flow<DownloadProgress> = flow {
        val remoteUrl = book.remoteUrl
            ?: return@flow emit(DownloadProgress.Error(IllegalStateException("Book has no remoteUrl")))

        val ext = BookFormat.toExt(book.format)

        val directory = localFilesDs.getBooksDirectory()
        val targetFile = File(directory, "${book.title}.$ext")

        remoteFileDs.downloadFile(
            fileUrl = remoteUrl,
            destination = targetFile
        ).collect { progress ->
            when (progress) {
                is DownloadProgress.Progress -> emit(progress)
                is DownloadProgress.Success -> {
                    val updatedBook = book.copy(localPath = targetFile.path)
                    localMetaDs.addBook(updatedBook)
                    emit(DownloadProgress.Success(targetFile))
                }
                is DownloadProgress.Error -> emit(DownloadProgress.Error(Exception("Download failed")))
            }
        }
    }


    override suspend fun deleteLocalBook(bookId: String): Unit = withContext(ioDispatcher) {
        val book = localMetaDs.getBookById(bookId)
            ?: return@withContext

        try {
            book.localPath?.let { localFilesDs.deleteFile(it) }
            localMetaDs.deleteBook(bookId)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteRemoteBook(bookId: String): Unit = withContext(ioDispatcher) {
        val book = remoteMetaDs.getBookById(bookId) ?: return@withContext

        book.remoteUrl?.let { remoteFileDs.deleteFile(it) }

        remoteMetaDs.deleteBook(bookId)

        localMetaDs.getBookById(bookId)?.let {
            it.localPath?.let { path -> localFilesDs.deleteFile(path) }
            localMetaDs.deleteBook(bookId)
        }
    }

    override suspend fun refreshBooks() = withContext(ioDispatcher) {

    }

    override suspend fun getBookById(bookId: String): Book? {
        val local = localMetaDs.getBookById(bookId)
        val remote = remoteMetaDs.getBookById(bookId)

        return when {
            local != null -> local
            remote != null -> remote
            else -> null
        }
    }

    override suspend fun searchBooks(query: String): List<Book> = withContext(ioDispatcher) {
        val local = localMetaDs.searchBooks(query)

        val auth = authRepository.getAuthStateAsFlow().first()
        val remote = if (auth is AuthState.Success) {
            remoteMetaDs.getBooks(auth.user.uid)
                .filter { it.title.contains(query, true) || (it.author?.contains(query, true) ?: false) }
        } else {
            emptyList()
        }
        val localIds = local.map { it.id }.toSet()

        return@withContext mutableListOf<Book>().apply {
            addAll(local)
            addAll(remote.filter { it.id !in localIds })
        }
    }

    override suspend fun getBookFile(book: Book): File? {
        val path = book.localPath ?: return null
        val file = localFilesDs.getFile(path)

        if (file == null) {
            localMetaDs.deleteBook(book.id)
        }

        return file
    }

    override suspend fun saveReadingProgress(bookId: String, @FloatRange(from = 0.0, to = 1.0) progress: Float) {
        val book = localMetaDs.getBookById(bookId) ?: return

        val file = book.localPath?.let { localFilesDs.getFile(it) } ?: run {
            localMetaDs.deleteBook(bookId)
            return
        }

        val safeProgress = progress.coerceIn(0f, 1f)
        localMetaDs.updateBook(book.copy(readProgress = safeProgress))
    }

    override suspend fun getReadingProgress(bookId: String): Float {
        val book = localMetaDs.getBookById(bookId) ?: return 0f

        val file = book.localPath?.let { localFilesDs.getFile(it) }
        if (file == null) {
            localMetaDs.deleteBook(bookId)
            return 0f
        }

        return book.readProgress
    }
}
