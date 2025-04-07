package com.amineaytac.lunessa.ui.type

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amineaytac.lunessa.R
import com.amineaytac.lunessa.core.data.model.Makeup
import com.amineaytac.lunessa.databinding.ItemTypesBinding
import com.amineaytac.lunessa.util.gone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target

class MakeupTypeAdapter(
    private val onItemClickListener: (item: Makeup) -> Unit
) : ListAdapter<Makeup, MakeupTypeAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(
        private val binding: ItemTypesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Makeup, position: Int) = with(binding) {

            val imageUrl = item.apiFeaturedImage
            val finalUrl = if (imageUrl.startsWith("//")) "https:$imageUrl" else imageUrl
            Glide.with(ivImage.context)
                .load(finalUrl)
                .listener(object : com.bumptech.glide.request.RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbTypeImage.gone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbTypeImage.gone()
                        return false
                    }
                })
                .placeholder(R.drawable.ic_makeup2)
                .into(ivImage)

            val brand = item.brand
            val name = item.name
            val fullText = "$brand $name"
            val spannable = SpannableString(fullText)

            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                brand.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                RelativeSizeSpan(1.2f),
                0,
                brand.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            tvBrandName.text = spannable
            tvPrice.text = "$${item.price}"

            clTypeItem.setOnClickListener {
                getItem(position)?.let {
                    onItemClickListener.invoke(it)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item, position)
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Makeup>() {
            override fun areItemsTheSame(
                oldItem: Makeup, newItem: Makeup
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Makeup, newItem: Makeup
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

