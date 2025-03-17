package com.amineaytac.lunessa.core.data.repo

import com.amineaytac.lunessa.core.data.model.Makeup
import com.amineaytac.lunessa.core.data.model.ProductsColors
import com.amineaytac.lunessa.core.network.dto.MakeupResponse
import com.amineaytac.lunessa.core.network.dto.ProductColor
import retrofit2.Response

typealias RestMakeupResponse = Response<MakeupResponse>
typealias TagListResponse = List<String?>?
typealias ProductColors = List<ProductColor?>?

fun RestMakeupResponse.toMakeupList(): List<Makeup> {
    return body()!!.map { it ->

        val productTypeFormatted = it.productType
            ?.replace("_", " ")
            ?.split(" ")
            ?.joinToString(" ") { word ->
                word.replaceFirstChar { it.uppercase() }
            }.orEmpty()

        val newBrand = it.brand?.split(" ")?.joinToString(" ") { word ->
            word.replaceFirstChar { char -> char.uppercase() }
        } ?: ""

        Makeup(
            it.id ?: -1,
            newBrand,
            it.name.orEmpty(),
            it.price.orEmpty(),
            it.priceSign.orEmpty(),
            it.imageLink.toString(),
            it.category.orEmpty(),
            productTypeFormatted,
            it.tagList.toTagList(),
            it.productColors.toColors()
        )
    }
}

fun TagListResponse.toTagList(): List<String> {
    return this?.map { tag ->
        tag?.split(" ")
            ?.joinToString(" ") { word ->
                if (word.firstOrNull()?.isLowerCase() == true) {
                    word.replaceFirstChar { it.uppercase() }
                } else {
                    word
                }
            } ?: ""
    } ?: emptyList()
}

fun ProductColors.toColors(): ProductsColors {
    return this?.firstOrNull { it != null }?.let { productColor ->
        ProductsColors(
            hexValue = productColor.hexValue,
            colourName = productColor.colourName
        )
    } ?: ProductsColors(null, null)
}