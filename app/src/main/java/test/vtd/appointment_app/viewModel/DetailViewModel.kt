package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.repository.DetailRepository
import test.vtd.appointment_app.repository.FavoriteRepository

class DetailViewModel: ViewModel(){
    private val detailRepository = DetailRepository()
    private val favoriteRepository = FavoriteRepository()
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    fun addFavoriteDoctor(item: Doctors) {
        favoriteRepository.load().observeForever { existingDoctors ->
            val isDuplicate = existingDoctors.any {
                it.picture == item.picture
            }
            if (!isDuplicate) {
                detailRepository.addFavoriteDoctor(
                    item,
                    onSuccess = {
                        _status.postValue("Success")
                    },
                    onFailure = { exception ->
                        _status.postValue("Error: ${exception.message}")
                    }
                )
            }else{
                _status.postValue("This doctor is existed")
            }
        }
    }

    fun makeAppointment(doctor:Doctors, time:String, note:String){
        detailRepository.makeAppointment(doctor,time, note,
            onSuccess = {
                _status.postValue("Success")
            },
            onFailure = { exception ->
                _status.postValue("Error: ${exception.message}")
            }
        )
    }

    override fun onCleared() {
        detailRepository.clearDisposables()
        super.onCleared()
    }
}