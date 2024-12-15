package test.vtd.appointment_app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import test.vtd.appointment_app.databinding.ItemFavoriteDoctorBinding
import test.vtd.appointment_app.model.Doctors

class FavoriteAdapter(private var iClickListener: IClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var mList:MutableList<Doctors> = mutableListOf()

    interface IClickListener{
        fun unFavorite(doctor:Doctors)
        fun onDoctorClick(doctor:Doctors)
    }

    class ViewHolder(val binding: ItemFavoriteDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems : List<Doctors>){
        mList.clear()
        mList.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFavoriteDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = mList[position]
        holder.binding.apply {
            tvName.text = doctor.name
            tvSpecial.text = doctor.special
            tvCost.text = doctor.cost

            Glide.with(holder.itemView.context)
                .load(doctor.picture)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imgAvatar)

            holder.binding.root.setOnClickListener {
                iClickListener.onDoctorClick(doctor)
            }
            holder.binding.imgHeart.setOnClickListener {
                iClickListener.unFavorite(doctor)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}