package test.task.firebase_storage.datasources

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import test.task.books.repositories.DownloadProgress
import test.task.datasources.BooksRemoteDataSource
import test.task.datasources.StorageResult
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume

class BooksRemoteDataSourceFirebaseStorageImpl @Inject constructor(
    private val storage: FirebaseStorage
) : BooksRemoteDataSource {

    override suspend fun uploadFile(fileUri: Uri, userId: String, fileName: String): Result<StorageResult> =
        suspendCancellableCoroutine { cont ->
            val ref = storage.reference.child("users/$userId/books/$fileName")
            val uploadTask = ref.putFile(fileUri)
            uploadTask.addOnSuccessListener { snapshot ->
                ref.downloadUrl.addOnSuccessListener { url ->
                    val result = StorageResult(
                        url = url.toString(),
                        fileName = fileName,
                        size = snapshot.metadata?.sizeBytes ?: 0L,
                        contentType = snapshot.metadata?.contentType ?: "application/octet-stream"
                    )
                    cont.resume(Result.success(result))
                }.addOnFailureListener { cont.resume(Result.failure(it)) }
            }.addOnFailureListener { cont.resume(Result.failure(it)) }
        }

    override suspend fun downloadFile(fileUrl: String, destination: File): Flow<DownloadProgress> =
        callbackFlow {
            val ref = storage.getReferenceFromUrl(fileUrl)
            val task = ref.getFile(destination)
            task.addOnProgressListener { snap ->
                val percent = if (snap.totalByteCount > 0)
                    (100 * snap.bytesTransferred / snap.totalByteCount).toInt()
                else 0
                trySend(DownloadProgress.Progress(percent))
            }.addOnSuccessListener {
                trySend(DownloadProgress.Success(destination))
                close()
            }.addOnFailureListener { e ->
                trySend(DownloadProgress.Error(e))
                close(e)
            }
            awaitClose { task.cancel() }
        }

    override suspend fun deleteFile(fileUrl: String): Result<Unit> = suspendCancellableCoroutine { cont ->
        val ref = storage.getReferenceFromUrl(fileUrl)
        ref.delete()
            .addOnSuccessListener { cont.resume(Result.success(Unit)) }
            .addOnFailureListener { cont.resume(Result.failure(it)) }
    }

    override suspend fun getFileUrl(filePath: String): String? {
        return try {
            storage.reference.child(filePath).downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
    }
}