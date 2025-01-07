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

    private val _checkAndSaveUser = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.DummyConstructor)
    val checkAndSaveUser get() = _checkAndSaveUser.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null) // UID 상태 추가
    val userId get() = _userId.asStateFlow()

    fun checkAndSaveUser(uid: String, userName: String, userImage: String) {
        _userId.value = uid
        viewModelScope.launch {
            loginRepository.checkAndSaveUser(uid, userName, userImage).collectLatest {
                _checkAndSaveUser.value = it
            }
        }
    }
}