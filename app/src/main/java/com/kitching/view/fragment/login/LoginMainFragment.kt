package com.kitching.view.fragment.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.kitching.BuildConfig
import com.kitching.common.BaseFragment
import com.kitching.common.throttleClicks
import com.kitching.data.datasource.PreferencesDataSource
import com.kitching.databinding.FragmentLoginMainBinding
import kotlinx.coroutines.launch
import com.kitching.R

class LoginMainFragment: BaseFragment<FragmentLoginMainBinding>(FragmentLoginMainBinding::inflate) {
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
            Toast.makeText(requireContext(), "로그인 실패: ${error.message}", Toast.LENGTH_SHORT).show()
        } else if (token != null) {
            fetchKakaoUserInfo()
        }
    }

    private fun fetchKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Toast.makeText(requireContext(), "유저 정보 가져오기 실패: ${error.message}", Toast.LENGTH_SHORT).show()
            } else if (user != null) {
                val kakaoUid = user.id.toString()
                val kakaoNickname = user.kakaoAccount?.profile?.nickname.orEmpty()
                val kakaoProfileImage = user.kakaoAccount?.profile?.profileImageUrl.orEmpty()

                saveUserToFireStore(kakaoUid, kakaoNickname, kakaoProfileImage)
            }
        }
    }

    private fun saveUserToFireStore(uid: String, name: String, imageUrl: String) {
        val userRef = Firebase.firestore.collection("user").document(uid)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    saveUserIdToDataStore(uid)
                } else {
                    val userMap = mapOf(
                        "id" to uid,
                        "userName" to name,
                        "userImage" to imageUrl
                    )

                    userRef.set(userMap)
                        .addOnSuccessListener {
                            saveUserIdToDataStore(uid)
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Firestore 저장 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Firestore 접근 실패: ${it.message}", Toast.LENGTH_SHORT).show()
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
}