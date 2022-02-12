package com.poid.marveldemoapp.data.remote

import com.poid.marveldemoapp.core.extension.toMd5Hash
import okhttp3.Interceptor
import okhttp3.Response

const val QUERY_PARAM_TS = "ts"
const val QUERY_PARAM_APIKEY = "apikey"
const val QUERY_PARAM_HASH = "hash"

class AuthInterceptor(
    private val timeStamp: String,
    private val publicKey: String,
    private val privatKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(QUERY_PARAM_TS, timeStamp)
            .addQueryParameter(QUERY_PARAM_APIKEY, publicKey)
            .addQueryParameter(QUERY_PARAM_HASH, "$timeStamp$privatKey$publicKey".toMd5Hash())
            .build()

        val requestBuilder = original.newBuilder().url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}