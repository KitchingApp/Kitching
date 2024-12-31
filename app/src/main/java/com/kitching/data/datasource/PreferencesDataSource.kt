package com.kitching.data.datasource

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "team")

class PreferencesDataSource(private val context: Context) {

    companion object {
        private val TEAM_ID = stringPreferencesKey("team_id")
    }

    suspend fun saveTeamId(teamId: String) {
        context.dataStore.edit { team -> team[TEAM_ID] = teamId }
    }

   suspend fun getTeamId() = context.dataStore.data.first()[TEAM_ID]
}