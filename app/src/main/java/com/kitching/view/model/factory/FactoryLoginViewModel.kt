package com.kitching.view.model.factory

import com.kitching.view.model.ScheduleViewModel

class FactoryLoginViewModel {
    companion object{
        private lateinit var scheduleViewModel: ScheduleViewModel

        fun fetchScheduleViewModel(): ScheduleViewModel {
            if (!Companion::scheduleViewModel.isInitialized) {
                scheduleViewModel = ScheduleViewModel()
            }
            return scheduleViewModel
        }
    }

}