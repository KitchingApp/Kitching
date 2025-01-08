package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.firebase.FirebaseResult
import com.kitching.data.repository.OtherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeViewModel(private val repository: OtherRepository = OtherRepository()) : ViewModel() {

    private val _notices = MutableStateFlow<FirebaseResult<MutableList<NoticeDTO>>>(FirebaseResult.Loading)
    val notices get() = _notices.asStateFlow()

    fun getNotices(teamId: String) {
        firebaseFlowHandler(_notices) {
            repository.getNotices(teamId)
        }
    }

    private val _createNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val createNoticeResult get() = _createNoticeResult.asStateFlow()

    fun createNotice(userId: String, teamId: String, title: String, content: String) {
        firebaseFlowHandler(_createNoticeResult) {
            repository.createNotice(userId, teamId, title, content)
        }
    }

    private val _updateNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val updateNoticeResult get() = _updateNoticeResult.asStateFlow()

    fun updateNotice(noticeId: String, title: String, content: String) {
        firebaseFlowHandler(_updateNoticeResult) {
            repository.updateNotice(noticeId, title, content)
        }
    }

    private val _deleteNoticeResult = MutableStateFlow<FirebaseResult<Boolean>>(FirebaseResult.Loading)
    val deleteNoticeResult get() = _deleteNoticeResult.asStateFlow()

    fun deleteNotice(noticeId: String) {
        firebaseFlowHandler(_deleteNoticeResult) {
            repository.deleteNotice(noticeId)
        }
    }

    private fun <T> firebaseFlowHandler(variable: MutableStateFlow<FirebaseResult<T>>, fetch: suspend () -> Flow<FirebaseResult<T>>) {
        return com.kitching.data.firebase.firebaseFlowHandler(variable, viewModelScope) {
            fetch()
        }
    }
}