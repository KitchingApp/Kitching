package com.kitchingapp.data.database.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.kitchingapp.common.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "team")

class PreferencesDataSourceImpl(private val context: Context): LocalDataSource {

    companion object {
        private val TEAM_ID = stringPreferencesKey("team_id")
    }

    override suspend fun saveTeamId(teamId: String) {
        context.dataStore.edit { team -> team[TEAM_ID] = teamId }
    }

   override val teamId = context.dataStore.data.map { it[TEAM_ID] }
}