package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.choijihyuk0609.plustalk1.R
import android.content.SharedPreferences
import android.widget.Button
import com.choijihyuk0609.plustalk1.presentation.view.auth.AuthActivity

class SettingFragment : Fragment() {

    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

        // 버튼 초기화
        logoutButton = view.findViewById(R.id.btn_logout)

        // 버튼 클릭 리스너 설정
        logoutButton.setOnClickListener {
            clearUserPreferences()
            navigateToAuthActivity()
        }

        return view
    }

    private fun clearUserPreferences() {
        // SharedPreferences에서 데이터 삭제
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    private fun navigateToAuthActivity() {
        // AuthActivity로 이동
        val intent = Intent(requireContext(), AuthActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // 현재 Activity 종료
    }
}