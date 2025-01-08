package com.kitching.view.fragment.other

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseDialog
import com.kitching.common.KitchingApplication
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.DialogConfirmBinding
import com.kitching.view.model.PrepViewModel
import kotlinx.coroutines.launch

class NoticeDeleteDialog:
    BaseDialog<DialogConfirmBinding>(DialogConfirmBinding::inflate) {

    private val viewModel = PrepViewModel.instance

    private val args: NoticeDeleteDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            messageTV.text = "공지를 삭제하시겠습니까?"

            with(confirmButton) {
                text = "삭제"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        val teamId = PreferencesDataSource(KitchingApplication.getAppContext()).getTeamId() ?: ""
                        viewModel.deletePrepCategory(args.noticeId)
                        viewModel.getPrepCategory(teamId)
                        findNavController().navigate(NoticeDeleteDialogDirections.actionNoticeDeleteDialogToNoticeFragment())
                        dismiss()
                    }
                }
            }

            with(cancelButton) {
                throttleClicks(viewLifecycleOwner) {
                    dismiss()
                }
            }
        }
    }
}