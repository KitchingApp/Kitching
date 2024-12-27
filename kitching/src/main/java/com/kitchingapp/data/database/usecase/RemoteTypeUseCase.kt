package com.kitchingapp.data.database.usecase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kitchingapp.data.database.datasource.FireStoreDataSourceImpl
import com.kitchingapp.data.database.repository.FireStoreRepositoryImpl

class RemoteTypeUseCase {
    companion object {
        fun selectRemoteType(remoteType: RemoteType) = if(remoteType == RemoteType.FIREBASE) {
            FireStoreRepositoryImpl(FireStoreDataSourceImpl(Firebase.firestore))
        } else {
            TODO("another remote type")
        }
    }
}