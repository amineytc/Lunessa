package com.amineaytac.lunessa.ui.filteritem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.core.data.model.FilterItem
import com.amineaytac.lunessa.databinding.ItemFilterBinding
import com.amineaytac.lunessa.util.invisible
import com.amineaytac.lunessa.util.visible

class FilterItemAdapter(
    private val onItemSelected: (String) -> Unit
) : ListAdapter<FilterItem, FilterItemAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterItem) = with(binding) {
            tvFilterItem.text = item.title

            if (item.isSelected) {
                tvFilterItem.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.pinkish_red)
                )
                ivYes.visible()
            } else {
                tvFilterItem.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.tealish_blue)
                )
                ivYes.invisible()
            }

            root.setOnClickListener {
                onItemSelected(item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FilterItem>() {
        override fun areItemsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: FilterItem, newItem: FilterItem): Boolean {
            return oldItem == newItem
        }
    }
}