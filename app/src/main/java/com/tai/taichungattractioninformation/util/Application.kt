package com.tai.taichungattractioninformation.util

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

class Application: Application() {
    val Context.dataStore by preferencesDataStore(name = "settings")
}