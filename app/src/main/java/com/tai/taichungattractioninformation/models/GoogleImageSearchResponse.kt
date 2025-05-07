package com.tai.taichungattractioninformation.models

data class GoogleImageSearchResponse(val items: List<GoogleImageItem>)
data class GoogleImageItem(val link: String)