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
}