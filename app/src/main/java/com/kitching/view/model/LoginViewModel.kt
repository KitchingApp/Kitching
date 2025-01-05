package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.TeamDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.LoginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository = LoginRepository()) : ViewModel() {
    private val _teamList = MutableStateFlow<FirebaseResult<MutableList<TeamDTO>>>(FirebaseResult.Loading)
    val teamList get() = _teamList.asStateFlow()

    fun getTeamList(userId: String) {
        viewModelScope.launch {
            loginRepository.getTeamList(userId).collectLatest {
                _teamList.value = it
            }
        }
    }
}