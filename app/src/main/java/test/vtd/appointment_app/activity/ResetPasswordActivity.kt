package test.vtd.appointment_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import test.vtd.appointment_app.databinding.ActivityResetPasswordBinding
import test.vtd.appointment_app.viewModel.ResetPasswordViewModel

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]

        getControls()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.status.observe(this){ status ->
            when(status){
                "success" -> {
                    val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    Toast.makeText(this, status, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getControls() {
        binding.apply {
            btnReset.setOnClickListener {
                val strEmail = emailReset.text?.trim().toString()
                viewModel.resetPassword(this@ResetPasswordActivity, strEmail)
            }
            imgBack.setOnClickListener {
                finish()
            }
        }
    }
}