package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.common.firebaseFlowHandler
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.NoticeRepositoryImpl
import com.kitching.data.repository.OtherRepository
import com.kitching.domain.repository.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeViewModel(private val repository: NoticeRepository = NoticeRepositoryImpl()) : ViewModel() {

    private val _notices = MutableStateFlow<FirebaseResult<List<NoticeDTO>>>(FirebaseResult.Loading)
    val notices get() = _notices.asStateFlow()

    fun getNotices(teamId: String) {
        viewModelScope.launch {
            repository.getNotices(teamId).collectLatest {
                _notices.value = it
            }
        }
    }

    private val _noticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Success(true))
    val noticeResult get() = _noticeResult.asStateFlow()

    fun createNotice(userId: String, teamId: String, title: String, content: String) {
        viewModelScope.launch {
            repository.createNotice(userId, teamId, title, content).collectLatest {
                _noticeResult.value = it
            }
        }
    }

//    private val _updateNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val updateNoticeResult get() = _updateNoticeResult.asStateFlow()

    fun updateNotice(noticeId: String, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNotice(noticeId, title, content).collectLatest {
                _noticeResult.value = it
            }
        }
    }

//    private val _deleteNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
//    val deleteNoticeResult get() = _deleteNoticeResult.asStateFlow()

    fun deleteNotice(noticeId: String) {
        viewModelScope.launch {
            repository.deleteNotice(noticeId).collectLatest {
                _noticeResult.value = it
            }
        }
    }
}