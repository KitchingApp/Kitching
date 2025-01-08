package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    private val _notices = MutableStateFlow<FirebaseResult<MutableList<NoticeDTO>>>(FirebaseResult.Loading)
    val notices get() = _notices.asStateFlow()

    fun getNotices(teamId: String) {
        viewModelScope.launch {
            repository.getNotices(teamId).collectLatest {
                _notices.value = it
            }
        }
    }

    private val _createNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createNoticeResult get() = _createNoticeResult.asStateFlow()

    fun createNotice(userId: String, teamId: String, title: String, content: String) {
        viewModelScope.launch {
            repository.createNotice(userId, teamId, title, content).collectLatest {
                _createNoticeResult.value = it
            }
        }
    }

    private val _updateNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updateNoticeResult get() = _updateNoticeResult.asStateFlow()

    fun updateNotice(noticeId: String, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNotice(noticeId, title, content).collectLatest {
                _updateNoticeResult.value = it
            }
        }
    }

    private val _deleteNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deleteNoticeResult get() = _deleteNoticeResult.asStateFlow()

    fun deleteNotice(noticeId: String) {
        viewModelScope.launch {
            repository.deleteNotice(noticeId).collectLatest {
                _deleteNoticeResult.value = it
            }
        }
    }
}