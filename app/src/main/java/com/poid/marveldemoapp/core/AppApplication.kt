package com.poid.marveldemoapp.core

import android.app.Application
import com.poid.marveldemoapp.data.local.MapperEntityToModel
import com.poid.marveldemoapp.data.local.MapperResponseToEntity
import com.poid.marveldemoapp.data.repository.ComicsRepository
import com.poid.marveldemoapp.data.remote.MapperResponseToModel
import com.poid.marveldemoapp.di.DataBaseComposition
import com.poid.marveldemoapp.di.MappersComposition
import com.poid.marveldemoapp.di.NetworkComposition

class AppApplication : Application() {

    private val dataBaseComposition: DataBaseComposition by lazy { DataBaseComposition(this) }
    private val networkComposition: NetworkComposition by lazy { NetworkComposition() }
    private val mappersComposition: MappersComposition by lazy { MappersComposition() }


    val repositoryComposition
        get() = ComicsRepository(
            networkComposition.apiDataSource,
            dataBaseComposition.dao,
            mappersComposition.mapperResponseToModel,
            mappersComposition.mapperResponseToEntity,
            mappersComposition.mapperEntityToModel
        )
}