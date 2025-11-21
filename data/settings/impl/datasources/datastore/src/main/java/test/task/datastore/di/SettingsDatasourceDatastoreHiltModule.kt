package test.task.datastore.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import test.task.datasources.SettingsDatasource
import test.task.datastore.datasources.SettingsDatasourceDatastoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsDatasourceDatastoreHiltModule {
    @Binds
    @Singleton
    abstract fun bindSettingsDatasourceDatastore(impl: SettingsDatasourceDatastoreImpl): SettingsDatasource
}