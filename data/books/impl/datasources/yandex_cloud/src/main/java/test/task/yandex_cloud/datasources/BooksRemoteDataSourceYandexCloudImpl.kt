package test.task.yandex_cloud.datasources

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import test.task.books.repositories.DownloadProgress
import test.task.datasources.BooksRemoteDataSource
import test.task.datasources.StorageResult
import java.io.File
import java.nio.file.Files
import javax.inject.Inject

class BooksRemoteDataSourceYandexCloudImpl @Inject constructor(
    private val config: YandexStorageConfig
) : BooksRemoteDataSource {

    private val s3: AmazonS3 by lazy {
        AmazonS3Client(
            BasicAWSCredentials(config.accessKey, config.secretKey),
            Region.getRegion(config.region)
        ).apply {
            setEndpoint("https://storage.yandexcloud.net")
        }
    }

    private fun buildObjectPath(userId: String, fileName: String): String =
        "users/$userId/books/$fileName"

    private fun buildPublicUrl(key: String): String =
        "https://${config.bucket}.storage.yandexcloud.net/$key"

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun uploadFile(
        fileUri: Uri,
        userId: String,
        fileName: String
    ): Result<StorageResult> {
        println("UUUU: $fileUri, $userId, $fileName")
        return try {
            val key = buildObjectPath(userId, fileName)

            val file = File(fileUri.path ?: return Result.failure(Exception("Invalid URI")))

            val meta = ObjectMetadata().apply {
                contentType = Files.probeContentType(file.toPath())
                contentLength = file.length()
            }
            println("UUUU: $key")
            s3.putObject(config.bucket, key, file.inputStream(), meta)
            println("UUUU: $key")

            Result.success(
                StorageResult(
                    url = buildPublicUrl(key),
                    fileName = fileName,
                    size = file.length(),
                    contentType = meta.contentType ?: "application/octet-stream"
                )
            )
        } catch (e: Exception) {
            println("UUUU!!: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun downloadFile(
        fileUrl: String,
        destination: File
    ): Flow<DownloadProgress> = callbackFlow {

        val key = fileUrl.substringAfter(".net/")

        trySend(DownloadProgress.Progress(0f))

        try {
            val s3Object = s3.getObject(config.bucket, key)
            val input = s3Object.objectContent
            val total = s3Object.objectMetadata.contentLength

            var downloaded = 0L
            destination.outputStream().use { out ->
                val buffer = ByteArray(8_192)
                while (true) {
                    val read = input.read(buffer)
                    if (read <= 0) break
                    out.write(buffer, 0, read)
                    downloaded += read
                    trySend(
                        DownloadProgress.Progress(
                            downloaded.toFloat() / total.toFloat()
                        )
                    )
                }
            }

            trySend(DownloadProgress.Success(destination))
            close()

        } catch (e: Exception) {
            trySend(DownloadProgress.Error(e))
            close(e)
        }

        awaitClose { }
    }

    override suspend fun deleteFile(fileUrl: String): Result<Unit> {
        return try {
            val key = fileUrl.substringAfter(".net/")
            s3.deleteObject(config.bucket, key)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFileUrl(filePath: String): String? {
        return buildPublicUrl(filePath)
    }
}

data class YandexStorageConfig(
    val bucket: String,
    val accessKey: String,
    val secretKey: String,
    val region: String = "ru-central1"
)
