package com.poid.marveldemoapp.data.remote

import com.poid.marveldemoapp.data.remote.entity.ComicsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ComicsApiService {

    @GET("comics")
    suspend fun fetchComics(
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String?,
    ): ComicsResponse

}