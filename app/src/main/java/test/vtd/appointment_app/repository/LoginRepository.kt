package test.vtd.appointment_app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import test.vtd.appointment_app.activity.LoginActivity
import test.vtd.appointment_app.model.Users
import test.vtd.appointment_app.retrofit.ApiAppointment
import test.vtd.appointment_app.retrofit.RetrofitClient
import test.vtd.appointment_app.sharedPreferences.mySharedPreferences
import test.vtd.appointment_app.utils.Utils

class LoginRepository {
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var compositeDisposable = CompositeDisposable()
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status
    private val apiAppointment:ApiAppointment =
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiAppointment::class.java)
    private var user = MutableLiveData<Users>()

    fun login(activity:LoginActivity ,strEmail:String, strPass:String){
        firebaseAuth.signInWithEmailAndPassword(strEmail, strPass)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    pullData(activity, strEmail)
                } else
                    _status.postValue(task.exception.toString())
            }
        }

    fun pullData(context: Context, strEmail: String): LiveData<Users> {
        compositeDisposable.add(apiAppointment.login(strEmail)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { userModel ->
                    if (userModel.success){
                        mySharedPreferences.setIsLogin(context, true)
                        Utils.user_current = userModel.result!![0]
                        user.value = (userModel.result!![0])
                        _status.postValue("success")
                    }
                },
                { error ->
                    _status.postValue(error.message.toString())
                }
            )
        )
        return user
    }
    fun updateUser(context: Context, newUser: Users){
        compositeDisposable.add(apiAppointment.updateUser(newUser.id, newUser.userName, newUser.dateOfBirth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { usersModel ->
                    if (usersModel.success){
                        Utils.user_current = newUser
                        mySharedPreferences.saveUser(context, newUser)
                        user.postValue(newUser)
                    }
                },
                { error ->
                    Log.e("UpdateUserError", "Error: ${error.message}")
                }
            )
        )
    }
    fun clearDisposables() {
        compositeDisposable.clear()
    }
}
