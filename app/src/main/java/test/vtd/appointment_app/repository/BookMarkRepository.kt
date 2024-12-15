package test.vtd.appointment_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.model.MakeAppointment
import test.vtd.appointment_app.retrofit.ApiAppointment
import test.vtd.appointment_app.retrofit.RetrofitClient
import test.vtd.appointment_app.utils.Utils

class BookMarkRepository {
    private val apiAppointment:ApiAppointment =
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiAppointment::class.java)
    private val compositeDisposable = CompositeDisposable()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val ref = firebaseDatabase.getReference("Doctors")
    private val listAppointment = MutableLiveData<MutableList<MakeAppointment>>()

    fun getAppointment():LiveData<MutableList<MakeAppointment>>{
        compositeDisposable.add(apiAppointment.getAppointment()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { makeAppointmentModel ->
                if(makeAppointmentModel.success && makeAppointmentModel.result!=null){
                    val filteredList = makeAppointmentModel.result ?: mutableListOf()
                    listAppointment.value = filteredList.toMutableList()
                }
            }
        )
        return listAppointment
    }

    fun getDoctorById(idDoctor: Int): LiveData<Doctors>{
        val doctor = MutableLiveData<Doctors>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children){
                    val item = child.getValue(Doctors::class.java)
                    if (item?.id  == idDoctor){
                        doctor.postValue(item)
                        return
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed to load data: ${error.message}")
            }

        })
        return doctor
    }

    fun changeStateAppointment(appointmentChange: MakeAppointment, reason: String){
        compositeDisposable.add(apiAppointment.changeStateAppointment(appointmentChange.id, 1, reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{ response ->
                if(response.success){
                    val currentList = listAppointment.value ?: mutableListOf()
                    val updatedList = currentList.map { appointment ->
                        if (appointment.id == appointmentChange.id) {
                            appointment.copy(state = 1, note = reason)
                        } else {
                            appointment
                        }
                    }
                    listAppointment.value = updatedList.toMutableList()
                }
            }
        )
    }

    fun clearDisposables() {
        compositeDisposable.clear()
    }
}