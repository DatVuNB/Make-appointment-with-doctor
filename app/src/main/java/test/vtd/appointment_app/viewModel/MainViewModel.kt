package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.repository.MainRepository

class MainViewModel: ViewModel() {
    private val repository = MainRepository()

    fun loadDoctors():LiveData<MutableList<Doctors>>{
        return repository.load()
    }

    fun filterDoctors(query : String):LiveData<MutableList<Doctors>>{
        return repository.filterDoctors(query)
    }
}