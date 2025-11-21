package test.task.localfiles.datasources

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import test.task.datasources.BooksLocalDataSource
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class BooksLocalDataSourceFilesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BooksLocalDataSource {

    private val booksDir: File by lazy {
        File(context.filesDir, "avito_books").apply { if (!exists()) mkdirs() }
    }

    override fun getBooksDirectory(): File = booksDir

    override suspend fun saveFile(inputStream: InputStream, fileName: String): File =
        withContext(Dispatchers.IO) {
            val dest = File(booksDir, fileName)
            inputStream.use { input ->
                FileOutputStream(dest).use { output ->
                    input.copyTo(output)
                }
            }
            dest
        }

    override suspend fun deleteFile(filePath: String): Boolean =
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            file.exists() && file.delete()
        }

    override suspend fun getFile(filePath: String): File? =
        withContext(Dispatchers.IO) {
            val f = File(filePath)
            if (f.exists()) f else null
        }

    override suspend fun getFileSize(filePath: String): Long =
        withContext(Dispatchers.IO) {
            val f = File(filePath)
            if (f.exists()) f.length() else 0L
        }
}