package com.poid.marveldemoapp.data.remote.entity

import com.squareup.moshi.Json

data class ComicsResponse(
    @field:Json(name = "data")
    val data: DataResponse?
)

data class DataResponse(
    @field:Json(name = "count")
    val count: String?,
    @field:Json(name = "limit")
    val limit: String?,
    @field:Json(name = "offset")
    val offset: String?,
    @field:Json(name = "results")
    val results: List<ComicsResultResponse>?,
    @field:Json(name = "total")
    val total: String?
)

data class ComicsResultResponse(
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "title")
    val title: String?,
    @field:Json(name = "description")
    val description: String?,
    @field:Json(name = "images")
    val images: List<ImageResponse>?,
    @field:Json(name = "prices")
    val prices: List<PriceResponse>?,
    @field:Json(name = "thumbnail")
    val thumbnail: ThumbnailResponse?,
    @field:Json(name = "variantDescription")
    val variantDescription: String?
)

data class ImageResponse(
    @field:Json(name = "extension")
    val extension: String?,
    @field:Json(name = "path")
    val path: String?
)

data class PriceResponse(
    @field:Json(name = "price")
    val price: String?,
    @field:Json(name = "type")
    val type: String?
)

data class ThumbnailResponse(
    @field:Json(name = "extension")
    val extension: String?,
    @field:Json(name = "path")
    val path: String?
)