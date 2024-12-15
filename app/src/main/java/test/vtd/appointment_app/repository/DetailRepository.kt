package test.vtd.appointment_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.retrofit.ApiAppointment
import test.vtd.appointment_app.retrofit.RetrofitClient
import test.vtd.appointment_app.utils.Utils

class DetailRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val apiAppointment: ApiAppointment =
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiAppointment::class.java)
    private val compositeDisposable = CompositeDisposable()

    fun addFavoriteDoctor(item: Doctors, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val ref = firebaseDatabase.getReference("Favorite")
        val key = item.id.toString()

        ref.child(key).setValue(item)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
    fun makeAppointment(doctor:Doctors, time:String, note:String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        compositeDisposable.add(apiAppointment.makeAppointment(
            doctor.id, Utils.user_current.id, time, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { makeAppointmentModel ->
                    if (makeAppointmentModel.success){
                        onSuccess()
                    }
                },
                { exception ->
                    onFailure(exception as Exception)
                }
            )
        )
    }
    fun clearDisposables() {
        compositeDisposable.clear()
    }
}