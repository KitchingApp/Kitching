package com.kitching.view.model.factory

import com.kitching.view.model.NoticeViewModel

class FactoryNoticeViewModel {
    companion object{
        private lateinit var noticeViewModel: NoticeViewModel

        fun fetchNoticeViewModel(): NoticeViewModel {
            if (!Companion::noticeViewModel.isInitialized) {
                noticeViewModel = NoticeViewModel()
            }
            return noticeViewModel
        }
    }

}