package com.poid.marveldemoapp.data.local

import com.poid.marveldemoapp.data.local.entity.ComicsEntity
import com.poid.marveldemoapp.data.remote.entity.ComicsResultResponse
import com.poid.marveldemoapp.data.remote.entity.PriceResponse
import com.poid.marveldemoapp.data.remote.entity.ThumbnailResponse
import java.util.*

class MapperResponseToEntity {
    fun map(from: ComicsResultResponse): ComicsEntity = ComicsEntity(
        id = from.id?.toInt() ?: UUID.randomUUID().variant(),
        title = from.title,
        description = from.description,
        prices = preparePrice(from.prices),
        thumbnailUrl = prepareImageUrl(from.thumbnail)
    )
}

private fun preparePrice(response: List<PriceResponse>?) =
    "${response?.get(0)?.type} ${response?.get(0)?.price}"

private fun prepareImageUrl(response: ThumbnailResponse?) =
    "${response?.path}.${response?.extension}"
        .replace("http:", "https:")