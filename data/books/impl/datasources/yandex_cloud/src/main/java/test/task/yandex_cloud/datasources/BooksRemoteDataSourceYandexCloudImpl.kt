package test.task.yandex_cloud.datasources

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.buffer
import okio.sink
import okio.source
import test.task.books.repositories.DownloadProgress
import test.task.datasources.BooksRemoteDataSource
import test.task.datasources.StorageResult
import test.task.yandex_cloud.AwsV4Signer
import test.task.yandex_cloud.YandexStorageConfig
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BooksRemoteDataSourceYandexCloudImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val config: YandexStorageConfig,
) : BooksRemoteDataSource {

    private val http = OkHttpClient()

    private val endpoint = "storage.yandexcloud.net"

    override suspend fun uploadFile(
        fileUri: Uri,
        userId: String,
        fileName: String
    ): Result<StorageResult> = withContext(Dispatchers.IO) {
        try {
            val objectKey = "$userId/$fileName"

            val input = context.contentResolver.openInputStream(fileUri)
                ?: return@withContext Result.failure(Exception("Unable to open file URI"))

            val bytes = input.readBytes()
            val sha256 = bytes.sha256Hex()

            val headers = AwsV4Signer.sign(
                method = "PUT",
                bucket = config.bucket,
                region = config.region,
                endpoint = endpoint,
                objectKey = objectKey,
                accessKey = config.accessKey,
                secretKey = config.secretKey,
                contentSha256 = sha256,
                headers = mutableMapOf()
            )

            val req = Request.Builder()
                .url("https://${config.bucket}.$endpoint/$objectKey")
                .put(bytes.toRequestBody(null))
                .apply { headers.forEach { addHeader(it.key, it.value) } }
                .build()

            val resp = http.newCall(req).execute()

            if (!resp.isSuccessful)
                return@withContext Result.failure(Exception("Upload failed: ${resp.code}"))

            val url = "https://${config.bucket}.$endpoint/$objectKey"

            Result.success(
                StorageResult(
                    url = url,
                    fileName = fileName,
                    size = bytes.size.toLong(),
                    contentType = resp.header("Content-Type") ?: "application/octet-stream"
                )
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun downloadFile(
        fileUrl: String,
        destination: File
    ): Flow<DownloadProgress> = callbackFlow {
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(fileUrl).get().build()
                val resp = http.newCall(request).execute()

                if (!resp.isSuccessful) {
                    trySend(DownloadProgress.Error(Exception("HTTP ${resp.code}")))
                    close()
                    return@withContext
                }

                val body = resp.body ?: run {
                    trySend(DownloadProgress.Error(Exception("Empty body")))
                    close()
                    return@withContext
                }

                val contentLength = body.contentLength()

                val source = body.source()
                val sink = destination.sink().buffer()
                val buffer = okio.Buffer()

                var total = 0L

                while (true) {
                    val read = source.read(buffer, 8_192)
                    if (read == -1L) break

                    sink.write(buffer, read)
                    sink.flush()

                    total += read

                    if (contentLength > 0) {
                        val percent = total.toFloat() / contentLength

                        trySend(
                            DownloadProgress.Progress(
                                percent = percent
                            )
                        )
                    } else {
                        trySend(DownloadProgress.Progress(percent = 0f))
                    }
                }

                sink.close()

                trySend(DownloadProgress.Success(destination))
                close()

            } catch (e: Exception) {
                trySend(DownloadProgress.Error(e))
                close(e)
            }
        }
    }



    override suspend fun deleteFile(fileUrl: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val bucket = config.bucket
            val objectKey = fileUrl.substringAfter("$bucket.$endpoint/")

            val headers = AwsV4Signer.sign(
                method = "DELETE",
                bucket = bucket,
                region = config.region,
                endpoint = endpoint,
                objectKey = objectKey,
                accessKey = config.accessKey,
                secretKey = config.secretKey,
                contentSha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", // empty SHA256
                headers = mutableMapOf()
            )

            val req = Request.Builder()
                .url("https://${bucket}.$endpoint/$objectKey")
                .delete()
                .apply { headers.forEach { addHeader(it.key, it.value) } }
                .build()

            val resp = http.newCall(req).execute()
            if (!resp.isSuccessful)
                return@withContext Result.failure(Exception("Delete failed: ${resp.code}"))

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFileUrl(filePath: String): String {
        return "https://${config.bucket}.$endpoint/$filePath"
    }
}

private fun ByteArray.sha256Hex(): String =
    java.security.MessageDigest.getInstance("SHA-256")
        .digest(this)
        .joinToString("") { "%02x".format(it) }