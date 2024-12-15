package test.vtd.appointment_app.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import test.vtd.appointment_app.databinding.ActivityProfileBinding
import test.vtd.appointment_app.databinding.DialogUpdateUserBinding
import test.vtd.appointment_app.model.Users
import test.vtd.appointment_app.utils.Utils
import test.vtd.appointment_app.viewModel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        getControls()
        observeViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.status.observe(this){
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewModel.pullData(this, Utils.user_current.email).observe(this@ProfileActivity) { userModel ->
            binding.apply {
                tvName.text = "Name: ${userModel.userName}"
                tvAge.text = "Date of birth: ${userModel.dateOfBirth}"
                tvEmail.text = "Email: ${userModel.email}"
            }
        }
    }

    private fun getControls() {
        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.logOut(this@ProfileActivity)
            }

            btnUpdate.setOnClickListener {
                val dialogView = DialogUpdateUserBinding.inflate(layoutInflater)
                dialogView.edtName.setText(Utils.user_current.userName)
                dialogView.edtDateOfBirth.setText(Utils.user_current.dateOfBirth)

                val builder = AlertDialog.Builder(this@ProfileActivity)
                builder.setView(dialogView.root)
                    .setTitle("UPDATE")
                    .setPositiveButton("Submit"){ _,_ ->
                        val newUser = Users(Utils.user_current.id, dialogView.edtName.text.toString(),
                            Utils.user_current.email, Utils.user_current.password,
                            dialogView.edtDateOfBirth.text.toString())
                        viewModel.updateUser(this@ProfileActivity, newUser)
                    }
                    .setNegativeButton("Cancel"){ dialog,_ ->
                        dialog.dismiss()
                    }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }
}