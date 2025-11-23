package test.task.yandex_cloud

data class YandexStorageConfig(
    val bucket: String,
    val accessKey: String,
    val secretKey: String,
    val region: String,
)