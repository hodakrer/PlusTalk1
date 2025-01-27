package com.example.plustalk1.presentation.view.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.plustalk1.R
import com.example.plustalk1.data.model.SignupRequest
import com.example.plustalk1.data.model.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class SignupFragment : Fragment() {

    //private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailInput = view.findViewById<EditText>(R.id.frSignup_emailInput)
        val passwordInput = view.findViewById<EditText>(R.id.frSignup_passwordInput)
        val signupButton = view.findViewById<Button>(R.id.frSignup_SignupButton)

        val Tag = "MyAppTag"
        Log.d(Tag, "This is a debug message")

        signupButton.setOnClickListener {
            // Read values from EditTexts when the button is clicked
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            // Create the signup request with the current values
            val signupRequest = SignupRequest(email = email, password = password)
            if (email.isNotEmpty() and password.isNotEmpty() ){
                performSignup(email, password)
            } else {
                Toast.makeText(requireContext(), "Please fill in the blanks", Toast.LENGTH_SHORT).show( )
            }
        }
    }

    fun performSignup(email : String, password : String){
        val signupRequest = SignupRequest(email, password)
        val retrofitCall = AuthActivity.RetrofitInstance.apiService.signup(signupRequest)
        retrofitCall.enqueue(object: Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {

                if(response.isSuccessful) {
                    val signupResponse = response.body( )
                    if(signupResponse != null){
                        val email     = signupResponse.email
                        val password  = signupResponse.signUpTime
                        val authority = signupResponse.authority
                        if(authority == "ROLE_MEMBER"){
                            //behaviours when signup is successful
                            Toast.makeText(requireContext( ), "Signed up successfully", Toast.LENGTH_SHORT).show( )
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, SigninFragment())
                                .addToBackStack(null)
                                .commit()
                        }else {
                            Toast.makeText(requireContext( ), "Failed Signing up", Toast.LENGTH_SHORT).show( )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}