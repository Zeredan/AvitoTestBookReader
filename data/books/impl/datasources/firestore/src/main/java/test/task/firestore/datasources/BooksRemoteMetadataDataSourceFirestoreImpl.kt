package test.task.firestore.datasources

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import test.task.books.Book
import test.task.books.BookFormat
import test.task.datasources.BooksRemoteMetadataDataSource
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class BooksRemoteMetadataDataSourceFirestoreImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    //private val auth: FirebaseAuth
) : BooksRemoteMetadataDataSource {

    private val collection get() = firestore.collection("books")

    private fun DocumentSnapshot.toBook(): Book {
        val id = id
        val title = getString("title") ?: "Без названия"
        val author = getString("author") ?: "Неизвестный автор"
        val remoteUrl = getString("remoteUrl")
        return Book(
            id = id,
            title = title,
            author = author,
            remoteUrl = remoteUrl,
            localPath = null,
            format = BookFormat.fromPath(remoteUrl.toString()) ?: BookFormat.TXT,
            readProgress = 0f
        )
    }

    override suspend fun getBooks(uid: String): List<Book> = suspendCancellableCoroutine { cont ->
        collection.whereEqualTo("userId", uid).get()
            .addOnSuccessListener { snap ->
                val items = snap.documents.mapNotNull { it.toBook() }
                cont.resume(items)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun getBooksAsFlow(uid: String) = callbackFlow {
        val listener = collection.whereEqualTo("userId", uid)
            .addSnapshotListener { snap, exc ->
                if (exc != null) {
                    close(exc)
                    return@addSnapshotListener
                }
                val items = snap?.documents?.mapNotNull { it.toBook() } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addBook(book: Book, uid: String): String = suspendCancellableCoroutine { cont ->
        val doc = collection.document()
        val map = mapOf(
            "title" to book.title,
            "author" to book.author,
            "remoteUrl" to book.remoteUrl,
            "userId" to uid,
        )
        doc.set(map)
            .addOnSuccessListener { cont.resume(doc.id) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun updateBook(bookId: String, book: Book) = suspendCancellableCoroutine { cont ->
        val map = mapOf<String, Any?>(
            "title" to book.title,
            "author" to book.author,
            "fileUrl" to book.remoteUrl
        )
        collection.document(bookId).update(map)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun deleteBook(bookId: String) = suspendCancellableCoroutine { cont ->
        collection.document(bookId).delete()
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun getBookById(bookId: String): Book? = suspendCancellableCoroutine { cont ->
        collection.document(bookId).get()
            .addOnSuccessListener { doc -> cont.resume(doc.toBook()) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}