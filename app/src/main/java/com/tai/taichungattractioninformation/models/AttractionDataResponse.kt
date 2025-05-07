package com.tai.taichungattractioninformation.models

import com.google.gson.annotations.SerializedName

data class AttractionDataResponseItem(
    @SerializedName("(選填)地圖服務連結")
    val mapServiceUrl: String,
    @SerializedName("景點中文名稱")
    val name: String,
    @SerializedName("景點中文地址")
    val address: String,
    @SerializedName("景點服務電話")
    val tel: String,
    @SerializedName("景點特色簡述(中文)")
    val introChinese: String,
    @SerializedName("景點特色簡述(英文)")
    val introEnglish: String,
    @SerializedName("景點英文名稱")
    val englishName: String,
    @SerializedName("景點英文地址")
    val englishAddress: String,
    @SerializedName("海域活動圖式編號")
    val seaAreaNo: String,
    @SerializedName("照片中文說明1")
    val photoChineseIntro: String,
    @SerializedName("照片連結網址1")
    val photoUrl: String,
    @SerializedName("經度")
    val wsgX: String,
    @SerializedName("網址")
    val url: String,
    @SerializedName("緯度")
    val wsgY: String,
    @SerializedName("設施圖示編號")
    val facilityNo: String,
    @SerializedName("開放時間")
    val openTime: String,
    @SerializedName("開放時間備註")
    val openTimeNote: String,
    @SerializedName("開放時間英文備註")
    val openTimeEnglishNote: String
)