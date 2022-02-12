package com.poid.marveldemoapp.data.remote

import com.poid.marveldemoapp.data.remote.entity.ComicsResultResponse
import com.poid.marveldemoapp.model.ComicsModel

class MapperResponseToModel {

    fun map(response: ComicsResultResponse): ComicsModel = ComicsModel(
        id = response.id.orEmpty(),
        title = response.title.orEmpty(),
        imageUrl = prepareImageUrl(response),
        description = response.description.orEmpty(),
        cost = prepareCost(response),
        isStarred = false
    )

    private fun prepareImageUrl(response: ComicsResultResponse) =
        "${response.thumbnail?.path}.${response.thumbnail?.extension}"
            .replace("http:", "https:")

    private fun prepareCost(response: ComicsResultResponse): String {
        val type = response.prices?.get(0)?.type
        val price = response.prices?.get(0)?.price

        return "$type $price"
    }
}