package com.kitching.view.model.factory

import com.kitching.view.model.PrepViewModel

class FactoryPrepViewModel {
    companion object{
        private lateinit var prepViewModel: PrepViewModel

        fun fetchPrepViewModel(): PrepViewModel {
            if (!Companion::prepViewModel.isInitialized) {
                prepViewModel = PrepViewModel()
            }
            return prepViewModel
        }
    }

}