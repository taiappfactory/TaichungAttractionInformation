package com.tai.taichungattractioninformation.models

import com.google.gson.annotations.SerializedName

data class FlowerDataResponseItem(
    @SerializedName("地址")
    val address: String,
    @SerializedName("地點")
    val location: String,
    @SerializedName("縣市")
    val city: String,
    @SerializedName("縣市別代碼")
    val cityCode: String,
    @SerializedName("花種")
    val flowerType: String,
    @SerializedName("行政區")
    val district: String,
    @SerializedName("行政區代碼")
    val districtCode: String,
    @SerializedName("觀賞時期")
    val viewingPeriod: String,
    @SerializedName("imageUrl")
    val imageUrl: String = ""
)