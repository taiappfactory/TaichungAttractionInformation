package com.tai.taichungattractioninformation.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

object LanguagePreference {
    private val Context.dataStore by preferencesDataStore("settings")
    private val LANGUAGE_KEY = stringPreferencesKey("language")

    suspend fun saveLanguage(context: Context, language: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = language }
    }

    fun getLanguageFlow(context: Context): Flow<String> {
        return context.dataStore.data.map { prefs -> prefs[LANGUAGE_KEY] ?: "zh" }
    }
}