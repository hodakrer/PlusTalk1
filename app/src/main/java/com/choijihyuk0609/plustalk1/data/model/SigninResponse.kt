package com.choijihyuk0609.plustalk1.data.model
data class SigninResponse(
    val status: Int,
    val message: String,
    val datas: SigninDatas?
)

data class SigninDatas(
    val email: String,
    val authority: String

)

/*
//Example Data
//When Login is Successful
{
  "status": 200,
  "message": "로그인 성공",
  "data": {
    "email": "qwe121@naver.com",
    "authority": "ROLE_MEMBER"
  }
}

//When There is No User Found
{
    "message": "회원 정보를 찾을 수 없습니다.",
    "data": "",
    "status": 401
}

//It doesn't Match With the Password
{
    "message": "비밀번호가 일치하지 않습니다.",
    "data": "",
    "status": 401
}
*/

