package test.vtd.appointment_app.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import test.vtd.appointment_app.model.Users

object mySharedPreferences {
    private val PREFS_NAME:String = "appointment_app_prefs"
    private val KEY_IS_LOGIN:String = "islogin"
    private val KEY_USER:String = "user"
    private val gson = Gson()

    private fun getPrefs(context: Context): SharedPreferences{
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setIsLogin(context: Context, isLogin:Boolean){
        val prefs:SharedPreferences = getPrefs(context)
        val editor:SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(KEY_IS_LOGIN, isLogin)
        editor.apply()
    }

    fun getIsLogin(context: Context):Boolean{
        val prefs:SharedPreferences = getPrefs(context)
        return prefs.getBoolean(KEY_IS_LOGIN, false)
    }

    fun saveUser(context: Context, users: Users){
        val prefs:SharedPreferences = getPrefs(context)
        val editor:SharedPreferences.Editor = prefs.edit()
        val json = gson.toJson(users)
        editor.putString(KEY_USER, json)
        editor.apply()
    }

    fun getUser(context: Context):Users?{
        val prefs:SharedPreferences = getPrefs(context)
        val json = prefs.getString(KEY_USER, null)
        return gson.fromJson(json, Users::class.java)
    }
}