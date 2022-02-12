package com.poid.marveldemoapp.presentation.wishList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poid.marveldemoapp.data.repository.ComicsRepository
import com.poid.marveldemoapp.model.ComicsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WishListViewModel(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    private val _comicsListFlow = MutableStateFlow<Result>(Result.Default)
    val comicsListFlow: StateFlow<Result> = _comicsListFlow

    init {
        observeStarredComics()
    }

    private fun observeStarredComics() = viewModelScope.launch(Dispatchers.IO) {
        comicsRepository.observeStarredComics()
            .collect {
                _comicsListFlow.value = Result.Success(it)
            }
    }

    fun setStarredComics(comics: ComicsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            comics.id?.let {
                comicsRepository.setComicsStarred(it, comics.isStarred ?: false)
            }
        }
    }

    fun searchByText(text: CharSequence?) {
        TODO("Not yet implemented")
    }

    sealed class Result {
        class Success(val items: List<ComicsModel>) : Result()
        class Failure(val error: Error) : Result()
        object Loading : Result()
        object Default : Result()
    }

    class Factory(
        private val comicsRepository: ComicsRepository
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            WishListViewModel(comicsRepository) as T
    }
}
