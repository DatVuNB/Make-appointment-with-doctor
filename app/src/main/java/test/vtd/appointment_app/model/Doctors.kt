package test.vtd.appointment_app.model

import android.os.Parcel
import android.os.Parcelable

data class Doctors(
    val address:String="",
    val biography:String="",
    val id:Int=0,
    val name:String="",
    val picture:String="",
    val special:String="",
    val expriense:Int=0,
    val cost:String="",
    val date:String="",
    val time:String="",
    val location:String="",
    val mobile:String="",
    val patiens:String="",
    val rating:Double=0.0,
    val site:String=""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(address)
        parcel.writeString(biography)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(picture)
        parcel.writeString(special)
        parcel.writeInt(expriense)
        parcel.writeString(cost)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(location)
        parcel.writeString(mobile)
        parcel.writeString(patiens)
        parcel.writeDouble(rating)
        parcel.writeString(site)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Doctors> {
        override fun createFromParcel(parcel: Parcel): Doctors {
            return Doctors(parcel)
        }

        override fun newArray(size: Int): Array<Doctors?> {
            return arrayOfNulls(size)
        }
    }
}
