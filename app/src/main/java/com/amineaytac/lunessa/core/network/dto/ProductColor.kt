package com.amineaytac.lunessa.core.network.dto


import com.google.gson.annotations.SerializedName

data class ProductColor(
    @SerializedName("colour_name")
    val colourName: String?,
    @SerializedName("hex_value")
    val hexValue: String?
)