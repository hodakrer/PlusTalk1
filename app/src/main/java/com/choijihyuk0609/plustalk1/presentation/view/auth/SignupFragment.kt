package com.choijihyuk0609.plustalk1.presentation.view.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.choijihyuk0609.plustalk1.R
import com.choijihyuk0609.plustalk1.data.model.SignupRequest
import com.choijihyuk0609.plustalk1.data.model.SignupResponse
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
    fun performSignup(email: String, password: String) {
        val signupRequest = SignupRequest(email, password)
        val retrofitCall = AuthActivity.RetrofitInstance.apiService.signup(signupRequest)

        retrofitCall.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful) {
                    // 회원가입 성공 처리
                    val responseBody = response.body()
                    val status = responseBody?.status
                    val message = responseBody?.message
                    val data = responseBody?.data
                    val email = data?.email
                    val signupTime = data?.signUpTime
                    val authority = data?.authority

                    Log.d("kkang", "status: $status \n message: $message \n data: $data \n" +
                            "email: $email \n signupTime: $signupTime \n authority: $authority")

                    if (status == 200) {
                        Toast.makeText(requireContext(), "Signed up successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, SigninFragment())
                            .addToBackStack(null)
                            .commit()
                    }

                } else {
                    // 회원가입 실패 처리 (예: 이메일 중복)
                    if (response.code() == 409) {
                        Toast.makeText(requireContext(), "The id already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed Signing up", Toast.LENGTH_SHORT).show()
                    }

                    Log.d("kkang", "Signup failed with code: ${response.code()} and message: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("kkang", "Signup request failed", t)
                Toast.makeText(requireContext(), "Network error, please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


/*
* {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                Log.d("kkang", "retrofitCall")
                Log.d("kkang", "${response.body( )}")
                if(response != null) {
                    Log.d("kkang", "response.isSuccessful")
                    val signupResponse = response.body( )

                    if(signupResponse != null){
                        val email     = signupResponse.data?.email
                        val password  = signupResponse.data?.signUpTime
                        val authority = signupResponse.data?.authority
                        if(signupResponse.status == 200) {
                            Log.d("kkang","status is 200")
                            //behaviours when signup is successful
                            Toast.makeText(
                                requireContext(),
                                "Signed up successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, SigninFragment())
                                .addToBackStack(null)
                                .commit()
                        } else if( signupResponse.status == 409){
                            Log.d("kkang","status is 409")
                            Toast.makeText(
                                requireContext(),
                                "The id already exists",
                                Toast.LENGTH_SHORT
                            ).show()
                        }else {
                            Log.d("kkang","Failed Signup")
                            Toast.makeText(requireContext( ), "Failed Signing up", Toast.LENGTH_SHORT).show( )
                        }
                    }
                } else {
                    Log.d("kkang", "Failed")
                    Log.d("kkang", "${response.body()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        }
* */