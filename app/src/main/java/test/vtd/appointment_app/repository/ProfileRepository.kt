package test.vtd.appointment_app.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import test.vtd.appointment_app.model.Users
import test.vtd.appointment_app.retrofit.ApiAppointment
import test.vtd.appointment_app.retrofit.RetrofitClient
import test.vtd.appointment_app.sharedPreferences.mySharedPreferences
import test.vtd.appointment_app.utils.Utils

class ProfileRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    fun logOut(context: Context){
        mySharedPreferences.setIsLogin(context, false)
        firebaseAuth.signOut()
        _status.postValue("success")
    }
}