package com.poid.marveldemoapp.presentation.wishList

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
import com.poid.marveldemoapp.R
import com.poid.marveldemoapp.core.AppApplication
import com.poid.marveldemoapp.core.base.BaseFragment
import com.poid.marveldemoapp.databinding.FragmentWishlistBinding
import com.poid.marveldemoapp.presentation.comics.ComicsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentWishList : BaseFragment() {

    private lateinit var binding: FragmentWishlistBinding
    private lateinit var viewModel: WishListViewModel
    private lateinit var factory: WishListViewModel.Factory
    private lateinit var comicsAdapter: ComicsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishlistBinding.inflate(inflater, container, false)
        setupAdapterView()
        setupSearchView()
        return binding.root
    }

    private fun setupSearchView() {
        binding.tiSearch.editText?.doOnTextChanged { text, _, _, _ ->
//         viewModel.searchByText(text)
        }
    }

    private fun initViewModel() {
        val appApplication = activity?.application?.applicationContext
        val repositoryComposition = (appApplication as AppApplication).repositoryComposition

        factory = WishListViewModel.Factory(repositoryComposition)
        viewModel = ViewModelProvider(this, factory)[WishListViewModel::class.java]
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
                        is WishListViewModel.Result.Loading -> showProgress()
                        is WishListViewModel.Result.Success -> showWishListSuccess(it)
                        is WishListViewModel.Result.Failure -> showError(it.error)
                        is WishListViewModel.Result.Default -> {/* nothing to show */
                        }
                    }
                }
            }
        }
    }

    private fun showWishListSuccess(it: WishListViewModel.Result.Success) {
        hideProgress()
        comicsAdapter.submitList(it.items)
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

