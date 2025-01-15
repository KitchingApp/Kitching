package com.kitching.view.model.factory

import com.kitching.view.model.DepartmentViewModel

class FactoryDepartmentViewModel {
    companion object{
        private lateinit var departmentViewModel: DepartmentViewModel

        fun fetchDepartmentViewModel(): DepartmentViewModel {
            if (!Companion::departmentViewModel.isInitialized) {
                departmentViewModel = DepartmentViewModel()
            }
            return departmentViewModel
        }
    }

}