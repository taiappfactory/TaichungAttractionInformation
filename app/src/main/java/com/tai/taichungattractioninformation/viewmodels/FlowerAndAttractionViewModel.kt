package com.tai.taichungattractioninformation.viewmodels

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.models.RestaurantDataResponseItem
import com.tai.taichungattractioninformation.repo.Repository
import com.tai.taichungattractioninformation.ui.MainActivity
import com.tai.taichungattractioninformation.util.LanguagePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlowerAndAttractionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = Repository()

    // Flower Data
    private val _flowerState = MutableStateFlow<List<FlowerDataResponseItem>>(emptyList())
    val flowerState: StateFlow<List<FlowerDataResponseItem>> = _flowerState

    // Attraction Data
    private val _attractionState = MutableStateFlow<List<AttractionDataResponseItem>>(emptyList())
    val attractionState: StateFlow<List<AttractionDataResponseItem>> = _attractionState

    private val _restaurantState = MutableStateFlow<List<RestaurantDataResponseItem>>(emptyList())
    val restaurantState: StateFlow<List<RestaurantDataResponseItem>> = _restaurantState

    // 多語系
    private val _languageState = MutableStateFlow("zh")
    val languageState: StateFlow<String> = _languageState

    // 搜尋花卉關鍵字
    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    // 啟動協程持續監聽使用者的語言偏好設定變更
    init {
        viewModelScope.launch {
            LanguagePreference.getLanguageFlow(application).collect {
                _languageState.value = it
            }
        }
    }

    // 切換語系
    fun toggleLanguage() {
        viewModelScope.launch {
            val newLang = if (_languageState.value == "zh") "en" else "zh"
            LanguagePreference.saveLanguage(getApplication(), newLang)

            // 建議：直接用 Activity 重啟的方式來套用新的 Context
            val context = getApplication<Application>()
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }

    // 取得賞花資訊
    fun getFlowerData() {
        viewModelScope.launch {
            try {
                // 取得花卉資料
                val response = repo.fetchFlowerList()

                // 更新狀態並顯示資料
                _flowerState.value = response
            } catch (e: Exception) {
                Log.e("Error", "MessageOut: ${e.message}")
            }
        }
    }

    // 取得海域景點資訊
    fun getAttractionData() {
        viewModelScope.launch {
            try {
                val response = repo.fetchAttractionList()
                val rawData = mutableListOf<AttractionDataResponseItem>()
                response.forEach {
                    rawData.add(it)
                }
                _attractionState.value = rawData
            } catch (e: Exception) {
                Log.e("Error", "MessageOut: ${e.message}")
            }
        }
    }

    // 取得餐廳資訊
    fun getRestaurantData() {
        viewModelScope.launch {
            try {
                // 取得花卉資料
                val response = repo.fetchRestaurantList()

                // 更新狀態並顯示資料
                _restaurantState.value = response
            } catch (e: Exception) {
                Log.e("Error", "MessageOut: ${e.message}")
            }
        }
    }

    fun updateSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun cleanSearchKeyword() {
        _searchKeyword.value = ""
    }

    // 回傳篩選後的花卉資料
    fun getFlowerFilteredData(): StateFlow<List<FlowerDataResponseItem>> =
        combine(_flowerState, _searchKeyword) { flowerList, keyword ->
            if (keyword.isBlank()) flowerList
            else flowerList.filter { it.flowerType.contains(keyword, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 回傳篩選後的餐廳資料
    fun getRestaurantFilteredData(): StateFlow<List<RestaurantDataResponseItem>> =
        combine(_restaurantState, _searchKeyword) { restaurantList, keyword ->
            if (keyword.isBlank()) restaurantList
            else restaurantList.filter { it.name.contains(keyword, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}