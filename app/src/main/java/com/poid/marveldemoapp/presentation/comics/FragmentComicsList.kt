package com.poid.marveldemoapp.presentation.comics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.poid.marveldemoapp.core.AppApplication
import com.poid.marveldemoapp.R
import com.poid.marveldemoapp.core.base.BaseFragment
import com.poid.marveldemoapp.databinding.FragmentComicsListBinding
import com.poid.marveldemoapp.model.ComicsModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentComicsList : BaseFragment() {

    private lateinit var binding: FragmentComicsListBinding
    private lateinit var viewModel: ComicsListViewModel
    private lateinit var factory: ComicsListViewModel.Factory
    private lateinit var comicsAdapter: ComicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComicsListBinding.inflate(inflater, container, false)
        setupAdapterView()
        setupSearchView()
        return binding.root
    }

    private fun setupSearchView() {
        binding.tiSearch.editText?.doOnTextChanged { text, _, _, _ ->
//            viewModel.searchByText(text)
        }
    }

    private fun initViewModel() {
        val appApplication = activity?.application?.applicationContext
        val repositoryComposition = (appApplication as AppApplication).repositoryComposition

        factory = ComicsListViewModel.Factory(repositoryComposition)
        viewModel = ViewModelProvider(this, factory)[ComicsListViewModel::class.java]
    }

    private fun setupAdapterView() {
        comicsAdapter = ComicsAdapter {
            viewModel.setStarredComics(it)
        }

        binding.list.apply {
            adapter = comicsAdapter
            val drawable = AppCompatResources.getDrawable(context, R.drawable.divider)
            val dividerItemDecoration = DividerItemDecoration(context, 1)
            drawable?.let { dividerItemDecoration.setDrawable(it) }
            addItemDecoration(dividerItemDecoration)
        }

        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.comicsListFlow.collectLatest {
                    when (it) {
                        is ComicsListViewModel.Result.Loading -> showProgress()
                        is ComicsListViewModel.Result.Success -> showListSuccess(it.items)
                        is ComicsListViewModel.Result.Failure -> showFailureMessage(it.error)
                        is ComicsListViewModel.Result.Default -> {/* nothing to show */ }
                    }
                }
                viewModel.searchListFlow.collect {
                    when (it) {
                        is ComicsListViewModel.Search.Success -> { showSearchResult(it.items) }
                        is ComicsListViewModel.Search.Empty -> { showEmptySearchResult()}
                        is ComicsListViewModel.Search.Default -> {/* nothing to show */ }
                    }
                }
            }
        }
    }

    private fun showFailureMessage(error: Error) {
        hideProgress()
        showError(error)
    }

    private fun showEmptySearchResult() {
        comicsAdapter.submitList(null)
        showToast("Nothing was found")
    }

    private fun showSearchResult(list: List<ComicsModel>) {
        comicsAdapter.submitList(null)
        showListSuccess(list)
    }

    private fun showListSuccess(list: List<ComicsModel>) {
        hideProgress()
        comicsAdapter.submitList(list)
    }

    override fun showProgress() {
        binding.progressCircular.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
    }

    override fun hideProgress() {
        binding.progressCircular.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }
}
