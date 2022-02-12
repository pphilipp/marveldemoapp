package com.poid.marveldemoapp.presentation.comics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poid.marveldemoapp.data.repository.ComicsRepository
import com.poid.marveldemoapp.data.remote.entity.ComicsRequest
import com.poid.marveldemoapp.data.remote.entity.ORDER_BY_TITLE
import com.poid.marveldemoapp.model.ComicsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ComicsListViewModel(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    private val _comicsListFlow = MutableStateFlow<Result>(Result.Default)
    var comicsListFlow: StateFlow<Result> = _comicsListFlow

    private val _searchListFlow = MutableStateFlow<Search>(Search.Default)
    var searchListFlow: StateFlow<Search> = _searchListFlow

    init {
        observeComicsSingleSourceOfTruth()
        getComicsList()
    }

    private fun observeComicsSingleSourceOfTruth() = viewModelScope.launch(Dispatchers.IO) {
        comicsRepository.observeComicsSingleSourceOfTruth()
            .collectLatest {
                if (it.isNotEmpty()) {
                    _comicsListFlow.value = Result.Success(it)
                }
            }
    }

    private fun getComicsList() = viewModelScope.launch(Dispatchers.IO) {
        _comicsListFlow.value = Result.Loading

        try {
            comicsRepository.getComicsListFlow(
                request = ComicsRequest(
                    limit = 10,
                    orderBy = ORDER_BY_TITLE,
                    dateRange = "2018-01-01,2021-01-01"
                )
            ).collect()
        } catch (e: Error) {
            _comicsListFlow.value = Result.Failure(e)
        }
    }

    fun setStarredComics(comics: ComicsModel) = viewModelScope.launch(Dispatchers.IO) {
        comics.id?.let {
            comicsRepository.setComicsStarred(it, comics.isStarred ?: false)
        }
    }

    fun searchByText(query: CharSequence?) = viewModelScope.launch(Dispatchers.IO) {
        query?.let {
            if (it.isNotBlank()) {
                comicsRepository
                    .search(query.toString())
                    .collect { _searchListFlow.value = Search.Success(it) }
            } else {
                _searchListFlow.value = Search.Empty
            }
        }
    }

    sealed class Result {
        class Success(val items: List<ComicsModel>) : Result()
        class Failure(val error: Error) : Result()
        object Loading : Result()
        object Default : Result()
    }
    sealed class Search {
        class Success(val items: List<ComicsModel>) : Search()
        object Empty : Search()
        object Default : Search()
    }

    class Factory(
        private val comicsRepository: ComicsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ComicsListViewModel(comicsRepository) as T
    }
}
