package com.kitching.view.fragment.other

import android.os.Bundle
import android.preference.Preference
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.kitching.adapter.NoticeAdapter
import com.kitching.common.BaseFragment
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.data.firebase.FirebaseResult
import com.kitching.databinding.FragmentNoticeBinding
import com.kitching.databinding.FragmentNoticeCreateBinding
import com.kitching.databinding.FragmentNoticeDetailBinding
import com.kitching.view.fragment.schedule.ScheduleCreateDialogArgs
import com.kitching.view.model.NoticeViewModel
import com.kitching.view.model.factory.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoticeUpdateFragment : BaseFragment<FragmentNoticeCreateBinding>(FragmentNoticeCreateBinding::inflate) {

    private val viewModel by viewModels<NoticeViewModel> {
        viewModelFactory
    }

    private val args: NoticeUpdateFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding)
        {
            noticeTitleTV.setText(args.notice.title)
            noticeContentTV.setText(args.notice.content)

            with(confirmBtn) {
                text = "수정"
                throttleClicks(viewLifecycleOwner) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        launch {
                            val teamId = PreferencesDataSource(requireContext()).getTeamId() ?: ""
                            val title = noticeTitleTV.text.toString()
                            val content = noticeContentTV.text.toString()
                            viewModel.updateNotice(args.notice.noticeId, title, content)
                            viewModel.getNotices(teamId)
                        }

                        launch {
                            viewModel.updateNoticeResult.collectLatest {
                                firebaseResultHandler(it) {
                                    findNavController().navigate(NoticeUpdateFragmentDirections.actionNoticeUpdateFragmentToNoticeFragment())
                                }
                            }
                        }
                    }
                }
            }
            with(cancelBtn) {
                text = "취소"
                throttleClicks(viewLifecycleOwner) {
                    findNavController().popBackStack()
                }   
            }
        }
    }
}