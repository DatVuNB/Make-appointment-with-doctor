package test.vtd.appointment_app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import test.vtd.appointment_app.model.Doctors

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val ref = firebaseDatabase.getReference("Doctors")
    fun load():LiveData<MutableList<Doctors>>{
        val listData = MutableLiveData<MutableList<Doctors>>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Doctors>()
                for(child in snapshot.children){
                    val item = child.getValue(Doctors::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Failed to load data: ${error.message}")
            }
        })
        return listData
    }
    fun filterDoctors(query : String):LiveData<MutableList<Doctors>>{
        val listData = MutableLiveData<MutableList<Doctors>>()
        ref.orderByChild("name").startAt(query).endAt(query + "\uf8ff").get()
            .addOnSuccessListener { snapshot ->
                val filteredList = mutableListOf<Doctors>()
                for (doctorSnapshot in snapshot.children) {
                    val doctor = doctorSnapshot.getValue(Doctors::class.java)
                    if (doctor != null) {
                        filteredList.add(doctor)
                    }
                }
                listData.value = filteredList
            }
        return listData
    }
}