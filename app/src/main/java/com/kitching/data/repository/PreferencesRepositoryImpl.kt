package com.kitching.data.repository

import com.kitching.data.datasource.PreferencesDataSourceImpl

class PreferencesRepositoryImpl(private val dataSource: PreferencesDataSourceImpl):
    LocalRepository {
    override suspend fun saveTeamId(teamId: String) {
        dataSource.saveTeamId(teamId)
    }

    override val teamId = dataSource.teamId

}