package com.kitching.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kitching.data.dto.NoticeDTO
import com.kitching.data.repository.RemoteRepository
import com.kitching.data.usecase.RemoteType
import com.kitching.data.usecase.RemoteTypeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoticeViewModel(private val remoteType: RemoteType) : ViewModel() {
    private val remoteRepository: RemoteRepository by lazy {
        RemoteTypeUseCase.selectRemoteType(remoteType)
    }

    private val _notices = MutableStateFlow<MutableList<NoticeDTO>>(mutableListOf<NoticeDTO>())
    val notices get() = _notices.asStateFlow()

    fun getNotices(teamId: String) {
        viewModelScope.launch {
            _notices.value = remoteRepository.getNotices(teamId)
        }
    }
}