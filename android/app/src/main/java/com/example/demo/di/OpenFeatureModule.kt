package com.example.demo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.openfeature.kotlin.sdk.Client
import dev.openfeature.kotlin.sdk.OpenFeatureAPI
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenFeatureModule {
    @Provides
    @Singleton
    fun provideOpenFeatureClient(): Client {
        return OpenFeatureAPI.getClient()
    }
}
