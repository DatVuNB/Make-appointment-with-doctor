package test.vtd.appointment_app.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.model.Users
import test.vtd.appointment_app.repository.LoginRepository
import test.vtd.appointment_app.repository.ProfileRepository

class ProfileViewModel:ViewModel() {
    private val profileRepository = ProfileRepository()
    private val loginRepository = LoginRepository()
    val status: LiveData<String> = profileRepository.status

    fun logOut(context: Context){
        profileRepository.logOut(context)
    }

    fun pullData(context: Context, strEmail: String): LiveData<Users>{
        return loginRepository.pullData(context, strEmail)
    }

    fun updateUser(context: Context, newUser: Users){
        loginRepository.updateUser(context, newUser)
    }

    override fun onCleared() {
        super.onCleared()
        loginRepository.clearDisposables()
    }
}