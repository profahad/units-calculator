package com.fahad.unitscalculator.base

import android.app.Application
import com.fahad.common_data.utils.CurrentActivityProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CurrentActivityProviderModule {

    @Provides
    @Singleton
    fun exposeCurrentActivityProvider(application: Application): CurrentActivityProvider = application as BaseApp
}
