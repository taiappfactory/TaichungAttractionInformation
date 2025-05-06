package com.tai.taichungattractioninformation.network

import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import retrofit2.http.GET

interface ApiService {
    @GET("f116d1db-56f7-4984-bad8-c82e383765c0")
    suspend fun getFlowerList(): List<FlowerDataResponseItem>
}