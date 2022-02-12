package com.poid.marveldemoapp.di

import com.poid.marveldemoapp.data.local.MapperEntityToModel
import com.poid.marveldemoapp.data.local.MapperResponseToEntity
import com.poid.marveldemoapp.data.remote.MapperResponseToModel

class MappersComposition {

    val mapperResponseToModel: MapperResponseToModel by lazy { MapperResponseToModel() }
    val mapperResponseToEntity: MapperResponseToEntity by lazy { MapperResponseToEntity() }
    val mapperEntityToModel: MapperEntityToModel by lazy { MapperEntityToModel() }
}