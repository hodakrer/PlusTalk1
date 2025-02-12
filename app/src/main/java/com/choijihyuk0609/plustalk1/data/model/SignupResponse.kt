package com.choijihyuk0609.plustalk1.data.model

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    val status: Int,
    val message: String,
    val datas: SignupDatas?
)

data class SignupDatas(
    val email: String,
    @SerializedName("signUpTime") val signUpTime: String, // JSON의 "signUpTime"과 매핑
    val authority: String
)

/*
//예시
{
  "status": 200,
  "message": "회원 가입 성공",
  "data": {
    "email": "qwe121@naver.com",
    "signUpTime": "2024-11-14T16:43:16",
    "authority": "ROLE_MEMBER"
  }
}

{
  "status": 409,
  "message": "이미 가입된 이메일",
  "data": ""
}
*/