package com.kitchingapp.view.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kitchingapp.data.database.dto.TeamDTO
import com.kitchingapp.data.database.repository.RemoteRepository
import com.kitchingapp.data.database.usecase.RemoteType
import com.kitchingapp.data.database.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamViewModel(private val remoteType: RemoteType) : ViewModel(),
    ViewModelProvider.Factory {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _teams = MutableStateFlow<List<TeamDTO>>(listOf<TeamDTO>())
    val teams get() = _teams.asStateFlow()

    fun getTeams(userId: String) {
        viewModelScope.launch {
            _teams.value = remoteRepository.getTeamsByUserId(userId)
        }
    }
}