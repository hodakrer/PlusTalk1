package com.choijihyuk0609.plustalk1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choijihyuk0609.plustalk1.data.model.SignupRequest
import com.choijihyuk0609.plustalk1.data.model.SignupResponse
import com.choijihyuk0609.plustalk1.data.repository.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel( ){
    val signupResult = MutableLiveData<Boolean>( )
    val errorMessage = MutableLiveData<String>( )
    val navigateToNextScreen = MutableLiveData<Boolean>( )

    fun signup(email : String, password : String){
        val signupRequest = SignupRequest(email, password)
        val retrofitCall  = RetrofitInstance.apiService.signup(signupRequest)
        if (email.isNotEmpty() && password.isNotEmpty() ){
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
                            errorMessage.value = "회원가입에 성공했습니다."
                            signupResult.value = true
                            navigateToNextScreen.value = true
                        }
                    } else {
                        // 회원가입 실패 처리 (예: 이메일 중복)
                        if (response.code() == 409) {
                            errorMessage.value = "중복된 아이디가 존재합니다."
                        } else {
                            errorMessage.value = "회원가입에 실패했습니다."
                        }
                    }
                }
                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    errorMessage.value = "네트워크 문제가 생겼습니다."
                }
            })
        } else {
            errorMessage.value ="빈칸을 채워주세요."
        }
    }
}