package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.TeamDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
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