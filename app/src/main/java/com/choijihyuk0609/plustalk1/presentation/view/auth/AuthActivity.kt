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

        if (savedInstanceState == null) {
            // Create a new instance of the LoginFragment
            val signinFragment = SigninFragment()

            // Use the FragmentManager to replace the container with the fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, signinFragment)
                .commit()
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