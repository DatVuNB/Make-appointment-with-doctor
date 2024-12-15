package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.activity.RegisterActivity
import test.vtd.appointment_app.repository.RegisterRepository

class RegisterViewModel: ViewModel() {
    private val repository = RegisterRepository()
    val status: LiveData<String> get() = repository.status

    fun register(activity: RegisterActivity, str_email: String, str_pass:String, str_name:String, str_date:String){
        repository.register(activity, str_email, str_pass, str_name, str_date)
    }
    override fun onCleared() {
        super.onCleared()
        repository.clearDisposables()
    }
}