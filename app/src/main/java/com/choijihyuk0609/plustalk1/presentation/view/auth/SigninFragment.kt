package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.SigninRequest
import com.choijihyuk0609.plustalk1.data.model.SigninResponse
import com.choijihyuk0609.plustalk1.presentation.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import retrofit2.converter.gson.GsonConverterFactory

class SigninFragment : Fragment(R.layout.fragment_signin) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Values View-Binded from Xml
        val emailInput = view.findViewById<EditText>(R.id.frSignin_emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.frSignin_passwordInput)
        val signinButton = view.findViewById<Button>(R.id.frSignin_SigninButton)
        val signupButton = view.findViewById<Button>(R.id.frSignin_SignupButton)

        //Handling Button signin -> Call 'performSignin' Function
        signinButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                Log.d(
                    "Signin",
                    "I CLICKED"
                )
                performSignin(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in the blanks", Toast.LENGTH_SHORT).show( )
            }
        }
        //Handling Button signup -> Move to SignupFragment
        signupButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignupFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    //Function Signin + NTW
    private fun performSignin(email: String, password: String) {
        Log.d(
            "Signin",
            "I CAME INTO performSignin function"
        )
        val signinRequest = SigninRequest(email, password)
        val retrofitCall  = AuthActivity.RetrofitInstance.apiService.signin(signinRequest)
        Log.d(
            "Signin",
            "Before enqueuing retrofit"
        )
        retrofitCall.enqueue(object: Callback<SigninResponse> {
            override fun onResponse(
                call: Call<SigninResponse>,
                response: Response<SigninResponse>
            ) {
                if (response.isSuccessful) {
                    val signinResponse = response.body()
                    // Log the entire response to see what it contains

                    if (signinResponse != null) {
                        // Access the fields
                        Toast.makeText(requireContext(), "Signin Successful", Toast.LENGTH_SHORT).show()
                        val status    = signinResponse.status
                        val message   = signinResponse.message
                        val datas     = signinResponse.datas

                        // Save Signin Info
                        saveUserInfo(email)

                        // Move to MainActivity
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }
                } else {
                    if (response.code() == 401){
                        Toast.makeText(requireContext(), "We can't find your id registered.", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle the error response
                        Toast.makeText(requireContext(), "Signin failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                // Handle network failure
                Toast.makeText(requireContext(), "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun saveUserInfo(email: String) {
        val preferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        preferences.edit().putString("email", email).apply( )
    }
}