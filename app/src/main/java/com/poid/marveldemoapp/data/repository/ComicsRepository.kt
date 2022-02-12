package com.poid.marveldemoapp.data.repository

import com.poid.marveldemoapp.data.local.ComicsDAO
import com.poid.marveldemoapp.data.local.MapperEntityToModel
import com.poid.marveldemoapp.data.local.MapperResponseToEntity
import com.poid.marveldemoapp.data.remote.datasorce.ApiDataSource
import com.poid.marveldemoapp.data.remote.MapperResponseToModel
import com.poid.marveldemoapp.data.remote.entity.ComicsRequest
import com.poid.marveldemoapp.core.extension.toInt
import com.poid.marveldemoapp.model.ComicsModel
import kotlinx.coroutines.flow.*

class ComicsRepository constructor(
    private val apiDataSource: ApiDataSource,
    private val comicsDAO: ComicsDAO,
    private val mapperResponseToModel: MapperResponseToModel,
    private val mapperResponseToEntity: MapperResponseToEntity,
    private val mapperEntityToModel: MapperEntityToModel
) {

    fun observeComicsSingleSourceOfTruth() = comicsDAO.loadAllComics()
        .map { listResp ->
            listResp
                .map { mapperEntityToModel.map(it) }
                .toList()
        }

    fun observeStarredComics() = comicsDAO.loadStarredComics()
        .map { listResp ->
            if (listResp.isEmpty()) {
                emptyList()
            } else {
                listResp
                    .map { mapperEntityToModel.map(it) }
                    .toList()
            }
        }

    fun getComicsListFlow(request: ComicsRequest): Flow<List<ComicsModel>> =
        apiDataSource.fetchComics(request)
            .onEach { list ->
                list.map { mapperResponseToEntity.map(it) }
                    .toList()
                    .also { comicsDAO.insertComics(it) }
            }
            .map { listResp ->
                listResp
                    .map { mapperResponseToModel.map(it) }
                    .toList()
            }

    fun search(query: String): Flow<List<ComicsModel>> =
        comicsDAO.searchComicsByText("*${query}*")
            .map { listResp ->
                listResp
                    .map { mapperEntityToModel.map(it) }
                    .toList()
            }

    fun setComicsStarred(comicsId: String, isStarred: Boolean) {
        comicsDAO.setComicsIsStarred(comicsId, isStarred.toInt())
    }
}
