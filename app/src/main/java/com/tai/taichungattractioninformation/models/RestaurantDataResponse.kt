package com.tai.taichungattractioninformation.models

import com.google.gson.annotations.SerializedName

data class RestaurantDataResponseItem(
    @SerializedName("停車資訊")
    val parkingInfo: String,
    @SerializedName("標題")
    val name: String,
    @SerializedName("簡介說明")
    val introduction: String,
    @SerializedName("經度")
    val wsgX: String,
    @SerializedName("緯度")
    val wsgY: String,
    @SerializedName("編號")
    val no: String,
    @SerializedName("電話")
    val tel: String
)