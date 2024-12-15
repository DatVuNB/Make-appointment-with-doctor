package test.vtd.appointment_app.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import test.vtd.appointment_app.R
import test.vtd.appointment_app.databinding.ActivityDetailBinding
import test.vtd.appointment_app.databinding.DialogMakeAppointmentBinding
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.viewModel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailViewModel
    private lateinit var binding:ActivityDetailBinding
    private lateinit var item:Doctors
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        getBundle()
        getControls()
    }

    private fun getControls() {
        binding.apply {
            btnBack.setOnClickListener{finish()}

            btnWebsite.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(item.site)
                startActivity(intent)
            }

            btnMessage.setOnClickListener{
                val uri = Uri.parse("smsto:${item.mobile}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "the sms text")
                startActivity(intent)
            }

            btnCall.setOnClickListener {
                val uri = Uri.parse("tel:${item.mobile.trim()}")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            }

            btnDirection.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.location))
                startActivity(intent)
            }

            btnShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text"
                intent.putExtra(Intent.EXTRA_SUBJECT, item.name)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    item.name + " " +item.address+" "+item.mobile
                )
                startActivity(Intent.createChooser(intent, "Choose one"))
            }

            imgHeart.setOnClickListener {
                viewModel.addFavoriteDoctor(item)
            }

            btnMakeAppointment.setOnClickListener {
                makeAppointment()
            }
        }
    }

    private fun makeAppointment() {
        val dialogView = DialogMakeAppointmentBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView.root)

        builder.setTitle("MAKE APPOINTMENT")
        builder.setPositiveButton("Submit"){ _,_ ->
            val time = dialogView.edtTime.text.toString()
            val note = dialogView.edtNote.text.toString()
            viewModel.makeAppointment(item, time, note)
        }
        builder.setNegativeButton("Cancel"){ dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    private fun getBundle() {
        item = intent.getParcelableExtra("object", Doctors::class.java)!!
        binding.apply {
            tvName.text = item.name
            tvSpecial.text = item.special
            tvPatiens.text = item.patiens
            tvBio.text = item.biography
            tvAddress.text = item.address
            tvDate.text = item.date
            tvTime.text = item.time
            tvExperiens.text = item.expriense.toString()
            tvRating.text = " ${item.rating}"

            Glide.with(this@DetailActivity)
                .load(item.picture)
                .into(imgPerson)

        }
    }
}