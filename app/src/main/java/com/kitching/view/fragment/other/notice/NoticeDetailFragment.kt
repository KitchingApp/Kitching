package com.kitching.view.fragment.other.notice

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kitching.common.BaseFragment
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentNoticeDetailBinding
import kotlinx.coroutines.launch

class NoticeDetailFragment :
    BaseFragment<FragmentNoticeDetailBinding>(FragmentNoticeDetailBinding::inflate) {

    private val args: NoticeDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding)
        {
            dateTV.text = args.notice.dateString
            writerTV.text = args.notice.writerName
            noticeTitleTV.text = args.notice.title

            with(noticeContentTV) {
                text = args.notice.content
                movementMethod = ScrollingMovementMethod.getInstance()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            if (args.notice.writerId == (PreferencesDataSource(requireContext()).getUserId()
                    ?: "")
            ) {
                val updateAction =
                    NoticeDetailFragmentDirections.actionNoticeDetailFragmentToNoticeUpdateFragment(
                        args.notice
                    )
                val deleteAction =
                    NoticeDetailFragmentDirections.actionNoticeDetailFragmentToNoticeDeleteDialog(
                        args.notice.noticeId
                    )
                val navController = findNavController()
                setOptionsActionBtn(
                    onClickUpdateBtn = { navController.navigate(updateAction) },
                    onClickDeleteBtn = { navController.navigate(deleteAction) }
                )
            }
        }
    }
}