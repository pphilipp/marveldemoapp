package com.poid.marveldemoapp.data.remote.datasorce

import com.poid.marveldemoapp.data.remote.ComicsApiService
import com.poid.marveldemoapp.data.remote.entity.ComicsRequest
import com.poid.marveldemoapp.data.remote.entity.ComicsResultResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ApiDataSource(
    private val apiService: ComicsApiService
) {

    fun fetchComics(
        request: ComicsRequest
    ): Flow<List<ComicsResultResponse>> = flow {
        try {
            val listResponse = apiService.fetchComics(request.limit, request.orderBy).data?.results

            if (listResponse.isNullOrEmpty()) {
                emit(emptyList())
            } else {
                emit(listResponse)
            }
        } catch (e: Throwable) {
            emit(emptyList())
        }
    }
}