package test.vtd.appointment_app.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import test.vtd.appointment_app.databinding.ActivityRegisterBinding
import test.vtd.appointment_app.viewModel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var viewModel:RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        getControls()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.status.observe(this){ status ->
            when (status) {
                "Success" -> {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                "Error" -> {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                    Log.e("error", status)
                }
                else -> {
                    Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
                    Log.e("error", status)
                }
            }
        }
    }

    private fun getControls() {
        binding.apply {
            btndangky.setOnClickListener {
                dangKy()
            }
            imgBack.setOnClickListener{
                finish()
            }
        }
    }

    private fun dangKy() {
        lateinit var strEmail:String
        lateinit var strName:String
        lateinit var strDate:String
        lateinit var strPass:String
        lateinit var strRepass:String
        binding.apply {
            strEmail = emailDky.text.toString().trim()
            strName = usernameDky.text.toString().trim()
            strDate = dateDky.text.toString().trim()
            strPass = passDky.text.toString().trim()
            strRepass = repassDky.text.toString().trim()
        }
        if (TextUtils.isEmpty(strEmail))
            Toast.makeText(applicationContext, "Please enter email", Toast.LENGTH_LONG).show()
        else if (strEmail.length < 6)
            Toast.makeText(applicationContext, "Email at least 6 character", Toast.LENGTH_SHORT).show()
        else if (TextUtils.isEmpty(strName))
            Toast.makeText(applicationContext, "Please enter username", Toast.LENGTH_SHORT).show()
        else if (TextUtils.isEmpty(strPass))
            Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_SHORT).show()
        else if (strPass.length < 6)
            Toast.makeText(applicationContext, "Password at least 6 character", Toast.LENGTH_SHORT).show()
        else if (TextUtils.isEmpty(strRepass))
            Toast.makeText(applicationContext, "Please enter repassword", Toast.LENGTH_SHORT)
                .show()
        else if (TextUtils.isEmpty(strDate))
            Toast.makeText(applicationContext, "Please enter date of birth", Toast.LENGTH_SHORT)
                .show()
        else {
            if(strPass == strRepass)
                viewModel.register(this,strEmail,strPass,strName,strDate)
            else
                Toast.makeText(applicationContext, "Password and repassword are not the same", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}