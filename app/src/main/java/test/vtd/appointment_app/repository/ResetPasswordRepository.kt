package test.vtd.appointment_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import test.vtd.appointment_app.activity.ResetPasswordActivity

class ResetPasswordRepository {
    private val _status = MutableLiveData<String>()
    val status:LiveData<String> get() = _status
    private val firebaseAuth:FirebaseAuth = FirebaseAuth.getInstance()

    fun resetPassword(activity: ResetPasswordActivity, strEmail:String){
        firebaseAuth.sendPasswordResetEmail(strEmail)
            .addOnCompleteListener(activity){ task ->
                if (task.isSuccessful){
                    _status.postValue("success")
                }
            }
            .addOnFailureListener(activity){ error ->
                _status.postValue(error.message)
            }
    }
}