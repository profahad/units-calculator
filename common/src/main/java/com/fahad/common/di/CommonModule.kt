package com.fahad.common.di

import android.content.Context
import com.fahad.common.providers.ResourceProviderImpl
import com.fahad.common.utils.ConnectivityChecker
import com.fahad.common_data.config.UrlProvider
import com.fahad.common_data.repository.CommonRepositoryImpl
import com.fahad.common_domain.providers.ResourceProvider
import com.fahad.common_domain.repository.CommonRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    @Singleton
    abstract fun bindCommonRepository(commonRepository: CommonRepositoryImpl): CommonRepository

    @Binds
    @Singleton
    abstract fun resourceProvider(resourceProviderImpl: ResourceProviderImpl): ResourceProvider

    companion object {

        @Provides
        @Singleton
        fun provideUrlProvider() = UrlProvider()

        /*@SuppressLint("MissingPermission")
        @Provides
        @Singleton
        fun provideFirebaseAnalytics(@ApplicationContext context: Context) =
            FirebaseAnalytics.getInstance(context)*/

        @Provides
        @Singleton
        fun provideConnectivityChecker(@ApplicationContext context: Context) =
            ConnectivityChecker(context)

    }
}

