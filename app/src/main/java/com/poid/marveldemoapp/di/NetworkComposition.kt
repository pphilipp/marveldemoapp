package com.poid.marveldemoapp.di

import com.poid.marveldemoapp.BuildConfig
import com.poid.marveldemoapp.data.remote.datasorce.ApiDataSource
import com.poid.marveldemoapp.data.remote.AuthInterceptor
import com.poid.marveldemoapp.data.remote.ComicsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class NetworkComposition {

    private val currentTimeStamp get() = System.currentTimeMillis()
    private val publicKey = "4600bf83b0f258f5f8087e19e50844b3"
    private val privatKey = "c1fa4dca1aabd7aca14039ea2f9adc20e2c45dd0"

    private val baseUrl by lazy { "https://gateway.marvel.com/v1/public/" }

    private val converterFactory by lazy { MoshiConverterFactory.create() }

    private val authInterceptor: AuthInterceptor by lazy {
        AuthInterceptor(
            currentTimeStamp.toString(),
            publicKey,
            privatKey
        )
    }

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        )
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }


    private val apiService: ComicsApiService by lazy {
        retrofit.create(ComicsApiService::class.java)
    }

    val apiDataSource: ApiDataSource = ApiDataSource(apiService)

}