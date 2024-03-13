package dev.stenglein.connectivitycontroller.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.stenglein.connectivitycontroller.data.source.connectivity.ConnectivityRepository
import dev.stenglein.connectivitycontroller.data.source.connectivity.FakeConnectivityRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ConnectivityModule::class]
)
object FakeConnectivityModule {

    // Use Provides instead of Binds because we need Provides in the default non-test module
    @Singleton
    @Provides
    fun provideConnectivityRepository(): ConnectivityRepository {
        return FakeConnectivityRepository()
    }
}