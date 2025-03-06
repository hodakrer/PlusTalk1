package com.choijihyuk0609.plustalk1.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choijihyuk0609.plustalk1.data.model.SigninRequest
import com.choijihyuk0609.plustalk1.data.model.SigninResponse
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel : ViewModel( ) {
    val loginResult = MutableLiveData<Boolean>( )
    val errorMessage = MutableLiveData<String>( )
    val navigateToNextScreen = MutableLiveData<Boolean>( )

    fun signin(email: String, password: String){
        val signinRequest = SigninRequest(email, password)
        val retrofitCall  = RetrofitInstance.apiService.signin(signinRequest)

        if(email.isNotEmpty() && password.isNotEmpty()) {
            retrofitCall.enqueue(object: Callback<SigninResponse> {
                override fun onResponse(
                    call: Call<SigninResponse>,
                    response: Response<SigninResponse>
                ) {
                    if (response.isSuccessful) {
                        val signinResponse = response.body()
                        // Log the entire response to see what it contains

                        if (signinResponse != null) {
                            //Toast.makeText(requireContext(), "Signin Successful", Toast.LENGTH_SHORT).show()
                            // Save Signin Info
                            loginResult.value = true
                            navigateToNextScreen.value = true
                        }
                    } else {
                        if (response.code() == 401){
                            errorMessage.value = "해당 사용자를 찾지 못했습니다."
                        } else {
                            // Handle the error response
                            errorMessage.value = "로그인에 실패했습니다."
                        }
                    }
                }
                override fun onFailure(call: Call<SigninResponse>, t: Throwable) {
                    // Handle network failure
                    errorMessage.value = "네트워크에 문제가 있었습니다."
                }
            })
        } else {
            errorMessage.value = "아이디와 비밀번호를 채워주세요."
        }
    }
}