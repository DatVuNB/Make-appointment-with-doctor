package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.activity.ResetPasswordActivity
import test.vtd.appointment_app.repository.ResetPasswordRepository

class ResetPasswordViewModel: ViewModel(){
    private val repository = ResetPasswordRepository()
    val status:LiveData<String> = repository.status
    fun resetPassword(activity: ResetPasswordActivity, strEmail:String){
        repository.resetPassword(activity, strEmail)
    }
}