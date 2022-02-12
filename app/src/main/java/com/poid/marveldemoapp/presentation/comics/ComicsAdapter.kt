package com.poid.marveldemoapp.presentation.comics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.poid.marveldemoapp.R
import com.poid.marveldemoapp.databinding.ItemMarvelPostBinding
import com.poid.marveldemoapp.model.ComicsModel

class ComicsAdapter(
    private val onClick: (ComicsModel) -> Unit
) : ListAdapter<ComicsModel, ComicsViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicsViewHolder {
        val binding = ItemMarvelPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComicsViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) =
        holder.bind(getItem(position))
}

class ComicsViewHolder(
    private val binding: ItemMarvelPostBinding,
    private val onClick: (ComicsModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ComicsModel) {
        binding.apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            tvCost.text = item.cost
            ivPicture.load(item.imageUrl) {
                placeholder(R.drawable.placeholder)
                transformations(RoundedCornersTransformation())
            }

            icFavorite.isSelected = item.isStarred ?: false
            icFavorite.setOnClickListener {
                val isSelected = !icFavorite.isSelected
                icFavorite.isSelected = isSelected
                item.isStarred = isSelected
                onClick(item)
            }
        }
    }
}

object DIFF_CALLBACK : DiffUtil.ItemCallback<ComicsModel>() {
    override fun areItemsTheSame(oldItem: ComicsModel, newItem: ComicsModel) =
        oldItem.title == newItem.title &&
                oldItem.description == newItem.description &&
                oldItem.isStarred == newItem.isStarred

    override fun areContentsTheSame(oldItem: ComicsModel, newItem: ComicsModel) =
        oldItem == newItem
}