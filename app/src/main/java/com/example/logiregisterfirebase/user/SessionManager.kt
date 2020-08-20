package com.example.logiregisterfirebase.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.logiregisterfirebase.MainActivity

class SessionManager {

    lateinit var preference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATE_MODE: Int = 0

    companion object{
        private val PREFERENCE_NAME = "SharedPreference"
        private val isLoggedKey = "isLoggedIn"
        private val uidKey = "uid"
        private val nameSurnameKey = "name_surname"
        private val emailKey = "email"
        private val passwordKey = "password"
        private val profileImgUrlKey = "profileImageUrl"
        //private val birthDayKey = "birthDay"
    }

    constructor(con:Context){
        this.con = con
        preference = con.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE)
        editor = preference.edit()
    }

    fun createLoginSession(user:User){
        editor.putBoolean(isLoggedKey,true)
        editor.putString(uidKey,user.uid)
        editor.putString(nameSurnameKey,user.name_surname)
        editor.putString(emailKey,user.email)
        editor.putString(passwordKey,user.password)
        editor.putString(profileImgUrlKey,user.profileImageUrl)

        editor.commit()

        editor.apply()
    }

    fun checkLogin(){
        if(!this.isLoggedIn()){
            var intent:Intent = Intent(con,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(intent)
        }
    }

    fun getUserDetails(): User{
        var user = User()
        preference.getString(uidKey,"")
        preference.getString(nameSurnameKey,"")
        preference.getString(emailKey,"")
        preference.getString(passwordKey,"")
        preference.getString(profileImgUrlKey,"")
        return user
    }

    fun LogouUser(){

        editor.clear()
        editor.commit()

        var intent: Intent = Intent(con,LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(intent)
    }
    fun isLoggedIn(): Boolean {
        return preference.getBoolean(isLoggedKey, false)
    }
}