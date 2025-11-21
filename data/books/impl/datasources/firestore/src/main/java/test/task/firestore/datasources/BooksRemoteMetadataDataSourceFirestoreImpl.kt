package test.task.firestore.datasources

class BooksRemoteMetadataDataSourceFirestoreImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : BooksRemoteMetadataDataSource {

    private val collection get() = firestore.collection("books")

    private fun DocumentSnapshot.toBookDomainOrNull(): Book? {
        val id = id
        val title = getString("title") ?: return null
        val author = getString("author") ?: ""
        val fileUrl = getString("fileUrl")
        val size = getLong("size")
        val createdAt = getLong("createdAt") ?: System.currentTimeMillis()
        val updatedAt = getLong("updatedAt") ?: createdAt
        val readProgress = (getDouble("readProgress") ?: 0.0).toFloat() // optional remote progress
        return Book(
            id = id,
            title = title,
            author = author,
            remoteFileUrl = fileUrl,
            localFilePath = null,
            size = size,
            isDownloaded = false,
            readProgress = readProgress,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    override suspend fun getBooks(): List<Book> = suspendCancellableCoroutine { cont ->
        collection.whereEqualTo("userId", auth.uid).get()
            .addOnSuccessListener { snap ->
                val items = snap.documents.mapNotNull { it.toBookDomainOrNull() }
                cont.resume(items)
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override fun getBooksAsFlow() = callbackFlow {
        val listener = collection.whereEqualTo("userId", auth.uid)
            .addSnapshotListener { snap, exc ->
                if (exc != null) {
                    close(exc)
                    return@addSnapshotListener
                }
                val items = snap?.documents?.mapNotNull { it.toBookDomainOrNull() } ?: emptyList()
                trySend(items)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun addBook(book: Book): String = suspendCancellableCoroutine { cont ->
        val doc = collection.document()
        val map = mapOf(
            "title" to book.title,
            "author" to book.author,
            "fileUrl" to book.remoteFileUrl,
            "size" to book.size,
            "userId" to auth.uid,
            "createdAt" to book.createdAt,
            "updatedAt" to book.updatedAt,
            "readProgress" to book.readProgress.toDouble()
        )
        doc.set(map)
            .addOnSuccessListener { cont.resume(doc.id) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun updateBook(bookId: String, book: Book) = suspendCancellableCoroutine<Unit> { cont ->
        val map = mapOf<String, Any?>(
            "title" to book.title,
            "author" to book.author,
            "fileUrl" to book.remoteFileUrl,
            "size" to book.size,
            "updatedAt" to System.currentTimeMillis(),
            "readProgress" to book.readProgress.toDouble()
        )
        collection.document(bookId).update(map)
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun deleteBook(bookId: String) = suspendCancellableCoroutine<Unit> { cont ->
        collection.document(bookId).delete()
            .addOnSuccessListener { cont.resume(Unit) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }

    override suspend fun getBookById(bookId: String): Book? = suspendCancellableCoroutine { cont ->
        collection.document(bookId).get()
            .addOnSuccessListener { doc -> cont.resume(doc?.toBookDomainOrNull()) }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}