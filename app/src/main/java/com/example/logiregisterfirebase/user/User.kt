package com.example.logiregisterfirebase.user

class User(var uid: String, var email: String, var name_surname: String, var password:String, var profileImageUrl: String){
    constructor():this("","","","","")
}