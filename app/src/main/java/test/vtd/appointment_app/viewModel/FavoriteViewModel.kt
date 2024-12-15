package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.repository.FavoriteRepository

class FavoriteViewModel:ViewModel() {
    private val repository = FavoriteRepository()

    fun loadData():LiveData<MutableList<Doctors>>{
        return repository.load()
    }

    fun unFavorite(doctor:Doctors){
        repository.unFavorite(doctor)
    }
}