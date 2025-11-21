package test.task.datasources

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import test.task.books.repositories.DownloadProgress
import java.io.File

interface BooksRemoteDataSource {
    suspend fun uploadFile(fileUri: Uri, userId: String, fileName: String): Result<StorageResult>
    suspend fun downloadFile(fileUrl: String, destination: File): Flow<DownloadProgress>
    suspend fun deleteFile(fileUrl: String): Result<Unit>
    suspend fun getFileUrl(filePath: String): String?
}


data class StorageResult(
    val url: String,
    val fileName: String,
    val size: Long,
    val contentType: String
)