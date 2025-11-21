package test.task.datasources

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface BooksRemoteDataSource {
    suspend fun uploadFile(fileUri: Uri, userId: String, fileName: String): Result<StorageResult>
    suspend fun downloadFile(fileUrl: String, destination: File): Flow<FileDownloadProgress>
    suspend fun deleteFile(fileUrl: String): Result<Unit>
    suspend fun getFileUrl(filePath: String): String?
}


data class StorageResult(
    val url: String,
    val fileName: String,
    val size: Long,
    val contentType: String
)

sealed class FileDownloadProgress {
    data class Progress(val percent: Int) : FileDownloadProgress()
    data class Success(val file: File) : FileDownloadProgress()
    data class Error(val exception: Exception) : FileDownloadProgress()
}