package com.kevinduran.myshop.di

import com.kevinduran.myshop.domain.providers.CurrentUserProvider
import com.kevinduran.myshop.domain.providers.TimerProvider
import com.kevinduran.myshop.domain.providers.UUIDProvider
import com.kevinduran.myshop.infrastructure.providers.DefaultUUIDProvider
import com.kevinduran.myshop.infrastructure.providers.FirebaseCurrentUserProvider
import com.kevinduran.myshop.infrastructure.providers.SystemTimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProvidersModule {

    @Binds
    abstract fun bindTimerProvider(
        provider: SystemTimeProvider
    ): TimerProvider

    @Binds
    abstract fun bindUUIDProvider(
        provider: DefaultUUIDProvider
    ): UUIDProvider

    @Binds
    abstract fun bindCurrentUserProvider(
        provider: FirebaseCurrentUserProvider
    ): CurrentUserProvider

}