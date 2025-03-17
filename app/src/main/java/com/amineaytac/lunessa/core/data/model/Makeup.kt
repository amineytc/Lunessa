package com.amineaytac.lunessa.core.data.model

data class Makeup(
    val id: Int,
    val brand: String,
    val name: String,
    val price: String,
    val priceSign: String,
    val imageLink: String,
    val category: String,
    val productType: String,
    val tagList: List<String>,
    val productColors: ProductsColors
)