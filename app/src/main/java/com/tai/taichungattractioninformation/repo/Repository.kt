package com.tai.taichungattractioninformation.repo

import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.network.ApiClient

class Repository {
    suspend fun fetchFlowerList(): List<FlowerDataResponseItem> {
        val response = ApiClient.retrofit.getFlowerList()
        return response.sortedBy { it.flowerType }
    }

    suspend fun fetchAttractionList(): List<AttractionDataResponseItem> {
        return ApiClient.retrofit.getAttractionList()
    }
}