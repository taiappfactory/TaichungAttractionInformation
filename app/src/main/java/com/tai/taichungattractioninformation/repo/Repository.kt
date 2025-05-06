package com.tai.taichungattractioninformation.repo

import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.network.ApiClient

class Repository {
    suspend fun fetchFlowerList(): List<FlowerDataResponseItem> {
        return ApiClient.retrofit.getFlowerList()
    }
}