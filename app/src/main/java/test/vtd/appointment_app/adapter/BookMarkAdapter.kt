package test.vtd.appointment_app.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import test.vtd.appointment_app.R
import test.vtd.appointment_app.activity.BookMarkActivity
import test.vtd.appointment_app.databinding.ItemAppointmentBinding
import test.vtd.appointment_app.model.MakeAppointment
import test.vtd.appointment_app.viewModel.BookMarkViewModel

class BookMarkAdapter(private val activity: BookMarkActivity, private val item: MutableList<MakeAppointment>,
    private val viewModel: BookMarkViewModel, private val iClickListener: IClickListener)
    : RecyclerView.Adapter<BookMarkAdapter.ViewHolder>(){

    class ViewHolder(val binding: ItemAppointmentBinding):
        RecyclerView.ViewHolder(binding.root)

    interface IClickListener{
        fun onCancelClick(appointment: MakeAppointment)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItem: List<MakeAppointment>){
        item.clear()
        item.addAll(newItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return item.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment = item[position]
        viewModel.getDoctorById(appointment.doctorId).observe(activity){ doctor ->
            holder.binding.apply {
                tvName.text = doctor?.name
                tvTime.text = "Time: ${appointment.time}"
                tvCost.text = doctor?.cost
                tvSpecial.text = doctor?.special
                tvAddress.text = "Address: ${doctor?.address}"
                tvMobilePhone.text = "Mobile: ${doctor?.mobile}"
                tvNote.text = "Note: ${appointment.note}"

                Glide.with(holder.itemView.context)
                    .load(doctor?.picture)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .into(imgAvatar)

                when (appointment.state) {
                    0 -> {
                        btnState.apply {
                            setBackgroundColor(ContextCompat.getColor(activity, R.color.red))
                            text = "CANCEL"
                        }
                    }
                    1 -> {
                        btnState.apply {
                            setBackgroundColor(ContextCompat.getColor(activity, R.color.grey))
                            text = "CANCELED"
                        }
                    }
                    else -> {
                        btnState.apply {
                            setBackgroundColor(ContextCompat.getColor(activity, R.color.darkGreen))
                            text = "CONFIRMED"
                        }
                    }
                }
            }
        }

        holder.binding.apply {
            btnState.setOnClickListener {
                iClickListener.onCancelClick(appointment)
            }
        }
    }
}