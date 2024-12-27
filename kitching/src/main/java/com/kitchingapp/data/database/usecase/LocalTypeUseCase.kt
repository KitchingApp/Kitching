package com.kitchingapp.data.database.usecase

import android.content.Context
import com.kitchingapp.data.database.datasource.PreferencesDataSourceImpl
import com.kitchingapp.data.database.repository.PreferencesRepositoryImpl

class LocalTypeUseCase(private val context: Context) {
    fun selectLocalType(localType: LocalType) = if (localType == LocalType.DATASTORE) {
        PreferencesRepositoryImpl(PreferencesDataSourceImpl(context))
    } else {
        TODO("another remote type")
    }
}