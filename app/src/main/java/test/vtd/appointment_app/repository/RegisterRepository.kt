package test.vtd.appointment_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import test.vtd.appointment_app.activity.RegisterActivity
import test.vtd.appointment_app.retrofit.ApiAppointment
import test.vtd.appointment_app.retrofit.RetrofitClient
import test.vtd.appointment_app.sharedPreferences.mySharedPreferences
import test.vtd.appointment_app.utils.Utils

class RegisterRepository {
    private val compositeDisposable = CompositeDisposable()
    private val apiAppointment:ApiAppointment =
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiAppointment::class.java)
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    fun register(activity: RegisterActivity, strEmail: String,strPass:String,strName:String,strDate:String) {
        firebaseAuth.createUserWithEmailAndPassword(strEmail, strPass)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    if (user != null) {
                        postData(activity, strEmail, strPass, strName, strDate, user.uid.toInt())
                    }else
                        _status.postValue("Error")
                }else{
                    _status.postValue(task.exception?.message)
                }
            }
    }

    private fun postData(context:RegisterActivity, strEmail: String, strPass: String,
                         strName: String, strDate: String, uid: Int) {
        compositeDisposable.add(apiAppointment.register(uid,strName,strEmail,strDate)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { userModel ->
                    if(userModel.success){
                        Utils.user_current.email = strEmail
                        Utils.user_current.password = strPass
                        mySharedPreferences.saveUser(context, Utils.user_current)
                        _status.postValue("Success")
                    }else
                        _status.postValue(userModel.message)
                },
                { error ->
                    _status.postValue(error.message)
                }
            )
        )
    }
    fun clearDisposables() {
        compositeDisposable.clear()
    }
}

