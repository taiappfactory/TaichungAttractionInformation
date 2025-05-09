package com.tai.taichungattractioninformation.network

import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.models.GoogleImageSearchResponse
import com.tai.taichungattractioninformation.models.RestaurantDataResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // 賞花
    @GET("f116d1db-56f7-4984-bad8-c82e383765c0")
    suspend fun getFlowerList(): List<FlowerDataResponseItem>

    // 遊憩景點
    @GET("38476e5e-9288-4b83-bb33-384b1b36c570")
    suspend fun getAttractionList(): List<AttractionDataResponseItem>

    // 餐聽
    @GET("2980962e-18e0-486c-909c-0618c0b18aa3")
    suspend fun getRestaurantList(): List<RestaurantDataResponseItem>
}