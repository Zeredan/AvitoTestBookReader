package test.task.yandex_cloud.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksRemoteDataSource
import test.task.yandex_cloud.YandexStorageConfig
import test.task.yandex_cloud.datasources.BooksRemoteDataSourceYandexCloudImpl
import java.net.URI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BooksRemoteDataSourceYandexCloudHiltModule {
    @Binds
    @Singleton
    abstract fun bindBooksRemoteDataSourceYandexCloudImpl(impl: BooksRemoteDataSourceYandexCloudImpl): BooksRemoteDataSource
}

@Module
@InstallIn(SingletonComponent::class)
class BooksRemoteDataSourceYandexCloudHiltModule2 {
    @Provides
    fun provideYandexConfig(): YandexStorageConfig = YandexStorageConfig(
        region = "ru-central1",
        bucket = "books-avito",
        accessKey = "YCAJEIZARAqPNoKvT5z9xbD2u", //да-да, можно хранить как-то иначе, в файлах
        secretKey = "YCPgSfA3JL_b-ZYozX5SyfNOdTNeqZbEz-5Jq0xX"
    )
}
