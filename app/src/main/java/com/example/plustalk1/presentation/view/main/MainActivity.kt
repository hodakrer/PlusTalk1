package com.example.plustalk1.presentation.view.main

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.plustalk1.R
import com.example.plustalk1.network.NetworkInterface
import com.example.plustalk1.presentation.view.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.Manifest
import android.os.Build

class MainActivity : AppCompatActivity() {
    // Request code for runtime permissions
    private val REQUEST_CODE_PERMISSIONS = 1001

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
        checkAndRequestPermissions()

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

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.READ_CONTACTS
        )

        // Add permissions conditionally based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, proceed with app functionality
            } else {
                // Handle permission denial (e.g., show a message to the user)
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Implementing RetrofitInstance by singleton pattern
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