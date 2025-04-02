package com.amineaytac.lunessa.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amineaytac.lunessa.core.data.model.TypeItem
import com.amineaytac.lunessa.databinding.ItemTypeBinding

class TypeAdapter(
    private val onItemClickListener: (item: TypeItem) -> Unit
) : ListAdapter<TypeItem, TypeAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(
        private val binding: ItemTypeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TypeItem, position: Int) = with(binding) {

            svgImage.setImageResource(item.svgResource)
            svgText.text = item.name

            clItem.setOnClickListener {
                getItem(position)?.let {
                    onItemClickListener.invoke(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item, position)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TypeItem>() {
            override fun areItemsTheSame(
                oldItem: TypeItem, newItem: TypeItem
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: TypeItem, newItem: TypeItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}