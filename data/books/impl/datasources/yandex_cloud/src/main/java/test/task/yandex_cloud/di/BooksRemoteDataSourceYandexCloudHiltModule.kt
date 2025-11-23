package test.task.yandex_cloud.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.BooksRemoteDataSource
import test.task.yandex_cloud.datasources.BooksRemoteDataSourceYandexCloudImpl
import test.task.yandex_cloud.datasources.YandexStorageConfig
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
        bucket = "your-bucket-name",
        accessKey = "YOUR_ACCESS_KEY",
        secretKey = "YOUR_SECRET_KEY"
    )
}
