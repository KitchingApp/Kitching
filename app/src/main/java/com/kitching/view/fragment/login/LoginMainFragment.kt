package com.kitching.view.fragment.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kitching.BuildConfig
import com.kitching.common.BaseFragment
import com.kitching.common.util.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentLoginMainBinding
import kotlinx.coroutines.launch
import com.kitching.R
import com.kitching.common.firebaseResultHandler
import com.kitching.view.model.LoginViewModel
import com.kitching.view.model.factory.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlin.getValue

class LoginMainFragment: BaseFragment<FragmentLoginMainBinding>(FragmentLoginMainBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 카카오 init 해줘야함
        KakaoSdk.init(this@LoginMainFragment.requireContext(), BuildConfig.NATIVE_APP_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kakaoLoginBtn.throttleClicks(viewLifecycleOwner) {
            performKakaoLogin()
        }

    }

    private fun performKakaoLogin() {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            // 카카오톡 앱을 통한 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                handleKakaoLoginResult(token, error)
            }
        } else {
            // 카카오 계정을 통한 로그인
            UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
                handleKakaoLoginResult(token, error)
            }
        }
    }

    private fun handleKakaoLoginResult(token: OAuthToken?, error: Throwable?) {
        if (error != null) {
            showError(error)
        } else if (token != null) {
            fetchKakaoUserInfo()
        }
    }

    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                showError(error)
            } else if (user != null) {
                val kakaoUid = user.id.toString()
                val kakaoNickname = user.kakaoAccount?.profile?.nickname.orEmpty()
                val kakaoProfileImage = user.kakaoAccount?.profile?.profileImageUrl.orEmpty()

                viewModel.checkAndSaveUser(kakaoUid, kakaoNickname, kakaoProfileImage)

                observeCheckAndSaveUser()
            }
        }
    }

    private fun observeCheckAndSaveUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.checkAndSaveUser.collectLatest {
                    firebaseResultHandler(it) {
                        viewModel.userId.value?.let { uid ->
                            saveUserIdToDataStore(uid)
                        }
                    }
                }
            }
        }
    }

    private fun saveUserIdToDataStore(userId: String) {
        lifecycleScope.launch {
            val preferencesDataSource = PreferencesDataSource(requireContext())
            preferencesDataSource.saveUserId(userId)
            navigateToLoginTeamsFragment()
        }
    }

    private fun navigateToLoginTeamsFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, LoginTeamsFragment())
            .commit()
    }

    private fun showError(error: Throwable) {
        Toast.makeText(requireContext(), "저장 실패: ${error.message}", Toast.LENGTH_SHORT).show()
    }
}