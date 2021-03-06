package com.tangpj.repository.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.apollographql.apollo.ApolloClient
import com.recurve.core.viewmodel.RecurevViewModelFactory
import com.tangpj.github.BuildConfig
import com.tangpj.github.core.apollo.DateCustomerAdapter
import com.tangpj.github.di.PagingConfig
import com.tangpj.repository.db.dao.RepoDao
import com.tangpj.repository.db.RepositoryDb
import com.tangpj.repository.type.CustomType
import dagger.Module
import dagger.Provides

@Module(includes = [
    ViewModelModule::class])
class RepositoryModule{

    @RepositoryScope
    @Provides
    fun providerRepositoryDb(app: Context) =
            Room.databaseBuilder(app, RepositoryDb::class.java, "repository.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @RepositoryScope
    @Provides
    fun providerRepoDao(repositoryDb: RepositoryDb): RepoDao =
            repositoryDb.repoDao()

    @RepositoryScope
    @Suppress("HasPlatformType")
    @Provides
    fun providerRepositoryApollo(
            apolloClientBuilder: ApolloClient.Builder,
            datetimeAdapter: DateCustomerAdapter) =
        apolloClientBuilder
                .serverUrl(BuildConfig.BASE_URL)
                .addCustomTypeAdapter(CustomType.DATETIME, datetimeAdapter)
                .build()

    @RepositoryScope
    @Provides
    fun providerRepoPagingConfig() = PagingConfig(
            pageSize = 10,
            initialLoadSizeHint = 20,
            enablePlaceholders = false)


    @RepositoryScope
    @Provides
    fun bindViewModelFactory(creators: Map<Class< out ViewModel>, @JvmSuppressWildcards ViewModel>)
            : ViewModelProvider.Factory = RecurevViewModelFactory(creators)

}