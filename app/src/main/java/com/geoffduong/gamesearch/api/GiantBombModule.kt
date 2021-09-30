package com.geoffduong.gamesearch.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object GiantBombModule {

    @Provides
    fun provideGiantBombService(): GiantBombService {
        return Retrofit.Builder().baseUrl("https://www.giantbomb.com")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                )
            )
            .build().create(GiantBombService::class.java)
    }
}