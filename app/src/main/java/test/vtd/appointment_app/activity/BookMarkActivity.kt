package test.vtd.appointment_app.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import test.vtd.appointment_app.adapter.BookMarkAdapter
import test.vtd.appointment_app.databinding.ActivityBookMarkBinding
import test.vtd.appointment_app.databinding.DialogCancelAppointmentBinding
import test.vtd.appointment_app.model.MakeAppointment
import test.vtd.appointment_app.viewModel.BookMarkViewModel

class BookMarkActivity : AppCompatActivity(), BookMarkAdapter.IClickListener {
    private lateinit var binding: ActivityBookMarkBinding
    private lateinit var viewModel: BookMarkViewModel
    private lateinit var adapter: BookMarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookMarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BookMarkViewModel::class.java]

        initRecyclerView()
        getControls()
    }

    private fun initRecyclerView() {
        adapter = BookMarkAdapter(this, mutableListOf(), viewModel, this)
        binding.rcvBookMark.layoutManager =
            LinearLayoutManager(this@BookMarkActivity, LinearLayoutManager.VERTICAL, false)
        binding.rcvBookMark.adapter = adapter
    }

    private fun getControls() {
        viewModel.getAppointment().observe(this){ appointment ->
            adapter.updateData(appointment)
        }
    }

    override fun onCancelClick(appointment: MakeAppointment) {
        if (appointment.state == 0){
            val dialogView = DialogCancelAppointmentBinding.inflate(layoutInflater)
            val builder = AlertDialog.Builder(this)
            builder.setView(dialogView.root)

            builder.setTitle("CANCEL APPOINTMENT")
            builder.setPositiveButton("Submit"){ _,_ ->
                val reason = dialogView.edtReason.text.toString()
                viewModel.changeStateAppointment(appointment, reason)
            }
            builder.setNegativeButton("Cancel"){ dialog,_ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
}