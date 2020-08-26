package com.example.logiregisterfirebase.user

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.logiregisterfirebase.ui.MainActivity

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
        Log.d("Seesionmqnqger", "Successfully logged in NAME : ${user.name_surname}")

        editor.putString(nameSurnameKey,user.name_surname)
        editor.putString(emailKey,user.email)
        editor.putString(passwordKey,user.password)
        editor.putString(profileImgUrlKey,user.profileImageUrl)

        editor.commit()
        editor.apply()

        Log.d("Seesionmqnqger", "Successfully logged in NAME : ${preference.getString(nameSurnameKey,"")}")

    }

    fun checkLogin(){
        if(!this.isLoggedIn()){
            var intent:Intent = Intent(con, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(intent)
        }
    }

    fun getUserDetails(): User{
        var user = User()
        user.uid=preference.getString(uidKey,"")!!
        user.name_surname=preference.getString(nameSurnameKey,"")!!
        user.email=preference.getString(emailKey,"")!!
        user.password=preference.getString(passwordKey,"")!!
        user.profileImageUrl=preference.getString(profileImgUrlKey,"")!!

        return user
    }

    fun LogOutUser():Boolean{
        editor.clear()
        editor.commit()

        return !isLoggedIn()
    }
    fun isLoggedIn(): Boolean {
        return preference.getBoolean(isLoggedKey, false)
    }
}