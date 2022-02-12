package com.poid.marveldemoapp.data.local

import com.poid.marveldemoapp.data.local.entity.ComicsEntity
import com.poid.marveldemoapp.model.ComicsModel

class MapperEntityToModel {

    fun map(from: ComicsEntity) = ComicsModel(
        id = from.id.toString(),
        imageUrl = from.thumbnailUrl,
        title = from.title,
        description = from.description,
        isStarred = from.isStarred,
        cost = from.prices
    )
}