package com.kitchingapp.data.database.repository

import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.datasource.PreferencesDataSourceImpl
import com.kitchingapp.data.database.dto.ScheduleDTO
import com.kitchingapp.domain.entities.Team

class PreferencesRepositoryImpl(private val dataSource: PreferencesDataSourceImpl): LocalRepository {

}