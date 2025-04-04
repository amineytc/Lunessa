package com.amineaytac.lunessa.core.data.model

import android.content.Context
import com.amineaytac.lunessa.R

data class TypeItem(val name: String, val svgResource: Int, val productType: String) {
    companion object {
        fun getDefaultList(context: Context) = listOf(
            TypeItem(context.getString(R.string.blush), R.drawable.ic_blush, "blush"),
            TypeItem(context.getString(R.string.lipliner), R.drawable.ic_lipliner, "lip_liner"),
            TypeItem(context.getString(R.string.lipstick), R.drawable.ic_lipstick, "lipstick"),
            TypeItem(context.getString(R.string.mascara), R.drawable.ic_mascara, "mascara"),
            TypeItem(context.getString(R.string.nailpolish), R.drawable.ic_nailpolish, "nail_polish"),
            TypeItem(context.getString(R.string.bronzer), R.drawable.ic_bronzer, "bronzer"),
            TypeItem(context.getString(R.string.eyebrow), R.drawable.ic_eyebrow, "eyebrow"),
            TypeItem(context.getString(R.string.eyeliner), R.drawable.ic_eyeliner, "eyeliner"),
            TypeItem(context.getString(R.string.eyeshadow), R.drawable.ic_eyeshadow, "eyeshadow"),
            TypeItem(context.getString(R.string.foundation), R.drawable.ic_foundation, "foundation")
        )
    }
}