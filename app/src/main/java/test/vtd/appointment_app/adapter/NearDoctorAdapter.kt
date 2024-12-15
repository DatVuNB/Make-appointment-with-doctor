package test.vtd.appointment_app.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import test.vtd.appointment_app.activity.DetailActivity
import test.vtd.appointment_app.databinding.ItemNearbyDoctorBinding
import test.vtd.appointment_app.model.Doctors

class NearDoctorAdapter(private val items: MutableList<Doctors>) :
    RecyclerView.Adapter<NearDoctorAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemNearbyDoctorBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<Doctors>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNearbyDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = items[position]
        holder.binding.apply {
            tvName.text = doctor.name
            tvCost.text = doctor.cost
            tvSpecial.text = doctor.special

            Glide.with(holder.itemView.context)
                .load(doctor.picture)
                .apply(RequestOptions().transform(CenterCrop()))
                .into(imgAvatar)

            holder.binding.root.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra("object", items[position])
                holder.itemView.context?.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = items.size

}
