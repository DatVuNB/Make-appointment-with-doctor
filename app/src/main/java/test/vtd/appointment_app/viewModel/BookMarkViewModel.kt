package test.vtd.appointment_app.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.model.MakeAppointment
import test.vtd.appointment_app.repository.BookMarkRepository

class BookMarkViewModel: ViewModel() {
    private val repository = BookMarkRepository()

    fun getAppointment(): LiveData<MutableList<MakeAppointment>> {
        return repository.getAppointment()
    }

    fun getDoctorById(idDoctor: Int): LiveData<Doctors>{
        return repository.getDoctorById(idDoctor)
    }

    fun changeStateAppointment(appointment: MakeAppointment, reason: String){
        repository.changeStateAppointment(appointment, reason)
    }

    override fun onCleared() {
        super.onCleared()
        repository.clearDisposables()
    }
}