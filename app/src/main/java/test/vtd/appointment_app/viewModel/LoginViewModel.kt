package test.vtd.appointment_app.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.activity.LoginActivity
import test.vtd.appointment_app.repository.LoginRepository
import test.vtd.appointment_app.sharedPreferences.mySharedPreferences

class LoginViewModel:ViewModel() {
    private val repository = LoginRepository()
    val status:LiveData<String> get() = repository.status

    fun login(activity: LoginActivity, strEmail:String, strPass:String){
        repository.login(activity, strEmail, strPass)
    }

    override fun onCleared() {
        repository.clearDisposables()
        super.onCleared()
    }
}