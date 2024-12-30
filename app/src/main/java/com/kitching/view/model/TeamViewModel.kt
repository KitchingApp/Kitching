package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: TeamRepository = TeamRepository()) : ViewModel() {
    private val _teams = MutableStateFlow<FirebaseResult<List<TeamDTO>>>(FirebaseResult.Loading)
    val teams get() = _teams.asStateFlow()

    fun getTeams(userId: String) {
        viewModelScope.launch {
             repository.getTeamsByUserId(userId).collectLatest {
                 _teams.value = it
             }
        }
    }
}