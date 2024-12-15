package test.vtd.appointment_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import test.vtd.appointment_app.model.Doctors

class FavoriteRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val ref = firebaseDatabase.getReference("Favorite")

    fun load():LiveData<MutableList<Doctors>>{
        val listData = MutableLiveData<MutableList<Doctors>>()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Doctors>()
                for(child in snapshot.children){
                    val item = child.getValue(Doctors::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return listData
    }

    fun unFavorite(doctor:Doctors){
        ref.orderByChild("id").equalTo(doctor.id.toDouble()).get().addOnSuccessListener { dataSnapshot ->
            for (childSnapshot in dataSnapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }
}