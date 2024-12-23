package com.kitchingapp.data.database.usecase

import android.content.Context
import com.kitchingapp.data.database.datasource.PreferencesDataSourceImpl
import com.kitchingapp.data.database.repository.PreferencesRepositoryImpl

class LocalTypeUseCase {
    companion object {
        fun selectLocalType(localType: LocalType, context: Context) = if(localType == LocalType.DATASTORE) {
            PreferencesRepositoryImpl(PreferencesDataSourceImpl(context))
        } else {
            TODO("another remote type")
        }
    }
}