package com.potatodevs.cropsamarica.di

import android.content.Context
import com.potatodevs.cropsamarica.datastore.FieldDataStore
import com.potatodevs.cropsamarica.datastore.LanguageDataStore
import com.potatodevs.cropsamarica.service.WeatherApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLanguageDataStore(
        @ApplicationContext context: Context
    ): LanguageDataStore = LanguageDataStore(context)


    @Provides
    @Singleton
    fun provideRiceFieldDatastore(
        @ApplicationContext context: Context
    ): FieldDataStore = FieldDataStore(context)


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Logs full request + response
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApiService(retrofit: Retrofit): WeatherApiService
            = retrofit.create(WeatherApiService::class.java)


}