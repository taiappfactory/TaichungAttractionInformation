package com.tai.taichungattractioninformation.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tai.taichungattractioninformation.models.AttractionDataResponseItem
import com.tai.taichungattractioninformation.models.FlowerDataResponseItem
import com.tai.taichungattractioninformation.repo.Repository
import com.tai.taichungattractioninformation.ui.MainActivity
import com.tai.taichungattractioninformation.util.LanguagePreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class FlowerAndAttractionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = Repository()

    // Flower Data
    private val _flowerState = MutableStateFlow<List<FlowerDataResponseItem>>(emptyList())
    val flowerState: StateFlow<List<FlowerDataResponseItem>> = _flowerState

    // Attraction Data
    private val _attractionState = MutableStateFlow<List<AttractionDataResponseItem>>(emptyList())
    val attractionState: StateFlow<List<AttractionDataResponseItem>> = _attractionState

    private val _languageState = MutableStateFlow("zh")
    val languageState: StateFlow<String> = _languageState

    init {
        viewModelScope.launch {
            LanguagePreference.getLanguageFlow(application).collect {
                _languageState.value = it
            }
        }
    }

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

    fun getAttractionData() {
        viewModelScope.launch {
            try {
                val response = repo.fetchAttractionList()
                val rawData = mutableListOf<AttractionDataResponseItem>()
                response.forEach {
                    rawData.add(it)
                }

                Log.d("taitest", "rawData: $rawData")

                _attractionState.value = rawData
            } catch (e: Exception) {
                Log.e("Error", "MessageOut: ${e.message}")
            }
        }
    }
}