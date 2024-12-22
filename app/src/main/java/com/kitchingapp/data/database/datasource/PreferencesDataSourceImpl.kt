package com.kitchingapp.data.database.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kitchingapp.common.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataSourceImpl(private val context: Context): LocalDataSource {

    private val dataStore = context.dataStore

    companion object {
        private val TEAM_ID = stringPreferencesKey("team_id")
    }

    override suspend fun saveTeamId(teamId: String) {
        dataStore.edit { team -> team[TEAM_ID] = teamId }
    }

    override suspend fun getTeamId(): Flow<String?> = dataStore.data.map { preferences -> preferences[TEAM_ID] }
}