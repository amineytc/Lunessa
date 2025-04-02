package com.amineaytac.lunessa.core.data.model

import android.content.Context
import com.amineaytac.lunessa.R

data class TypeItem(val name: String, val svgResource: Int) {
    companion object {
        fun getDefaultList(context: Context) = listOf(
            TypeItem(context.getString(R.string.blush), R.drawable.ic_blush),
            TypeItem(context.getString(R.string.lipliner), R.drawable.ic_lipliner),
            TypeItem(context.getString(R.string.lipstick), R.drawable.ic_lipstick),
            TypeItem(context.getString(R.string.mascara), R.drawable.ic_mascara),
            TypeItem(context.getString(R.string.nailpolish), R.drawable.ic_nailpolish),
            TypeItem(context.getString(R.string.bronzer), R.drawable.ic_bronzer),
            TypeItem(context.getString(R.string.eyebrow), R.drawable.ic_eyebrow),
            TypeItem(context.getString(R.string.eyeliner), R.drawable.ic_eyeliner),
            TypeItem(context.getString(R.string.eyeshadow), R.drawable.ic_eyeshadow),
            TypeItem(context.getString(R.string.foundation), R.drawable.ic_foundation)
        )
    }
}