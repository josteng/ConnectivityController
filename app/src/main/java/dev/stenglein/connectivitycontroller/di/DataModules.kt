package dev.stenglein.connectivitycontroller.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepository
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {
    @Singleton
    @Provides
    fun provideConnectivityRepository(
        @ApplicationContext context: Context
    ): ConnectivityRepository {
        return ConnectivityRepositoryImpl.newInstance(context)
    }
}