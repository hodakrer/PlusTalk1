package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.presentation.view.main.MainActivity
import com.choijihyuk0609.plustalk1.presentation.viewmodel.SigninViewModel

class SigninFragment : Fragment(R.layout.fragment_signin) {
    private lateinit var viewModel: SigninViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SigninViewModel :: class.java)
        //Values View-Binded from Xml
        val emailInput = view.findViewById<EditText>(R.id.frSignin_emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.frSignin_passwordInput)
        val signinButton = view.findViewById<Button>(R.id.frSignin_SigninButton)
        val signupButton = view.findViewById<Button>(R.id.frSignin_SignupButton)

        signinButton.setOnClickListener {
            viewModel.signin(emailInput.text.toString( ), passwordInput.text.toString() )
        }

        //Handling Button signup -> Move to SignupFragment
        signupButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignupFragment())
                .addToBackStack(null)
                .commit()
        }

        // 로그인 결과 옵저빙
        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                val email = emailInput.text.toString()
                saveUserInfo(email)  // 로그인 성공 시 이메일 저장
                // 로그인 성공 후 이동 (예: MainActivity로 이동)
                startActivity(Intent(requireContext(), MainActivity::class.java))
            } else {
                // 로그인 실패 시 처리 (Toast 또는 에러 메시지)
                Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserInfo(email: String) {
        val preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        preferences.edit().putString("email", email).apply( )
    }
}