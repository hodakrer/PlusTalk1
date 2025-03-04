package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.choijihyuk0609.plustalk1.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.choijihyuk0609.plustalk1.network.NetworkInterface


class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // SigninFragment를 초기 화면으로 로드
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SigninFragment()) // Fragment를 지정한 container에 추가
            transaction.commit()
        }

    }

    object RetrofitInstance {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder( )
                .baseUrl("https://rapapa.site/")
                .addConverterFactory(GsonConverterFactory.create( ) )
                .build( )
        }
        val apiService: NetworkInterface by lazy {
            retrofit.create(NetworkInterface::class.java)
        }
    }
}