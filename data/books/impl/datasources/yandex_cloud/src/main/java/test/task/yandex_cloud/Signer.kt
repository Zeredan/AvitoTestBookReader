package test.task.yandex_cloud

import okio.ByteString.Companion.encodeUtf8
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

// Этот ужас написал за меня Чат Гпт.
object AwsV4Signer {

    fun sign(
        method: String,
        bucket: String,
        region: String,
        endpoint: String,
        objectKey: String,
        accessKey: String,
        secretKey: String,
        contentSha256: String,
        headers: MutableMap<String, String>
    ): Map<String, String> {

        val host = "$bucket.$endpoint"
        headers["Host"] = host

        val date = amzDate()
        val shortDate = date.substring(0, 8)
        headers["x-amz-date"] = date
        headers["x-amz-content-sha256"] = contentSha256

        val canonicalHeaders = headers
            .toSortedMap()
            .entries
            .joinToString("\n") { "${it.key.lowercase()}:${it.value.trim()}" } + "\n"

        val signedHeaders = headers.keys
            .map { it.lowercase() }
            .sorted()
            .joinToString(";")

        val canonicalRequest = listOf(
            method,
            "/$objectKey",
            "",
            canonicalHeaders,
            signedHeaders,
            contentSha256
        ).joinToString("\n")

        val credentialScope = "$shortDate/$region/s3/aws4_request"
        val stringToSign = listOf(
            "AWS4-HMAC-SHA256",
            date,
            credentialScope,
            canonicalRequest.sha256()
        ).joinToString("\n")

        val signingKey = signingKey(secretKey, shortDate, region, "s3")
        val signature = hmacSha256(signingKey, stringToSign.encodeUtf8().toByteArray())
            .hex()

        val authorizationHeader =
            "AWS4-HMAC-SHA256 Credential=$accessKey/$credentialScope, SignedHeaders=$signedHeaders, Signature=$signature"

        return mapOf(
            "Authorization" to authorizationHeader,
            "x-amz-date" to date,
            "x-amz-content-sha256" to contentSha256,
            "Host" to host
        )
    }

    private fun amzDate(): String =
        SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

    private fun signingKey(secret: String, date: String, region: String, service: String): ByteArray {
        val kDate = hmacSha256(("AWS4$secret").toByteArray(UTF_8), date.toByteArray(UTF_8))
        val kRegion = hmacSha256(kDate, region.toByteArray(UTF_8))
        val kService = hmacSha256(kRegion, service.toByteArray(UTF_8))
        return hmacSha256(kService, "aws4_request".toByteArray(UTF_8))
    }

    private fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray =
        Mac.getInstance("HmacSHA256").apply { init(SignatureKey(key)) }.doFinal(data)

    private fun ByteArray.hex(): String = joinToString("") { "%02x".format(it) }

    private fun String.sha256(): String =
        java.security.MessageDigest.getInstance("SHA-256")
            .digest(this.toByteArray(UTF_8))
            .joinToString("") { "%02x".format(it) }

    private class SignatureKey(key: ByteArray) : SecretKeySpec(key, "HmacSHA256")
}
