package com.kitching.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kitching.common.dataStore
import kotlinx.coroutines.flow.first

class PreferencesDataSource(private val context: Context) {

    companion object {
        private val TEAM_ID = stringPreferencesKey("team_id")
        private val USER_ID = stringPreferencesKey("user_id")
    }

    suspend fun saveTeamId(teamId: String) {
        context.dataStore.edit { preferences -> preferences[TEAM_ID] = teamId }
    }

    suspend fun getTeamId(): String? = context.dataStore.data.first()[TEAM_ID]

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences -> preferences[USER_ID] = userId }
    }

    suspend fun getUserId(): String? = context.dataStore.data.first()[USER_ID]
}
