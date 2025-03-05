package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.choijihyuk0609.plustalk1.R

//  AuthActivity.kt
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        // 로그인하지 않았다면 SigninFragment로 이동.
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SigninFragment())
            transaction.commit()
        }
    }
}