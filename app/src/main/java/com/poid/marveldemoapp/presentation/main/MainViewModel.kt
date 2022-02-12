package com.poid.marveldemoapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poid.marveldemoapp.data.repository.ComicsRepository
import com.poid.marveldemoapp.model.ComicsModel
import com.poid.marveldemoapp.presentation.comics.ComicsListViewModel
import com.poid.marveldemoapp.presentation.wishList.WishListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    private val _comicsCounterFlow =
        MutableStateFlow<Result>(Result.Default)
    val comicsCounterFlow: StateFlow<Result> = _comicsCounterFlow

    init {
        observeStarredComics()
    }

    private fun observeStarredComics() = viewModelScope.launch(Dispatchers.IO) {
        comicsRepository.observeStarredComics()
            .collectLatest {
                _comicsCounterFlow.value = Result.Success(it.size)
            }
    }

    sealed class Result {
        class Success(val count: Int) : Result()
        object Default : Result()
    }

    class Factory(
        private val comicsRepository: ComicsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(comicsRepository) as T
    }
}