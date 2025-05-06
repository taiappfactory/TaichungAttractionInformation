package com.tai.taichungattractioninformation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.repo.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlowerAndAttractionViewModel: ViewModel() {
    private val Repo = Repository()

    private val _flowerState = MutableStateFlow<List<FlowerDataResponseItem>>(emptyList())
    val flowerState: StateFlow<List<FlowerDataResponseItem>> = _flowerState

    fun getFlowerData() {
        viewModelScope.launch {
            try {
                val response = Repo.fetchFlowerList()
                _flowerState.value = response
            } catch (e: Exception) {
                Log.e("FlowerViewModel", "Error: ${e.message}")
            }
        }
    }
}