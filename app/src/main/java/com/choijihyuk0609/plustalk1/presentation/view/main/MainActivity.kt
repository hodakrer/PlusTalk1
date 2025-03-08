package com.choijihyuk0609.plustalk1.presentation.view.main

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.network.NetworkInterface
import com.choijihyuk0609.plustalk1.presentation.view.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import android.os.Build
import com.choijihyuk0609.plustalk1.utils.PermissionRequest

class MainActivity : AppCompatActivity() {
    //PermissionRequest code on utils package
    private lateinit var permissionRequest: PermissionRequest
    //Variables
    private val friendFragment = FriendFragment()
    private val chatFragment = ChatFragment()
    private val imageFragment = ImageFragment()
    private val settingFragment = SettingFragment()

    //Lifecycle onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check and request permissions at the start
        permissionRequest = PermissionRequest(this)
        permissionRequest.checkAndRequestPermissions()

        // Check if user is logged in
        if (!isUserLoggedIn()) {
            startNextActivity()  // Move to AuthActivity if user is not logged in
        }

        //set fragments
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, friendFragment)
            .commit()

        // Bottom Navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_friends -> {
                    switchFragment(friendFragment)
                    true
                }
                R.id.navigation_chat -> {
                    switchFragment(chatFragment)
                    true
                }
                R.id.navigation_images -> {
                    switchFragment(imageFragment)
                    true
                }
                R.id.navigation_settings -> {
                    switchFragment(settingFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Function to check if the user is logged in
    private fun isUserLoggedIn(): Boolean {
        // Use SharedPreferences to check if login info is saved
        val preferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        return preferences.contains("email")
    }
    // If user didnt login
    private fun startNextActivity() {
        // Create an Intent to start the next activity
        val intent = Intent(this, AuthActivity::class.java)

        startActivity(intent)

        finish()
    }

    // Helper function to replace the fragments
    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}