package test.vtd.appointment_app.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.paperdb.Paper
import test.vtd.appointment_app.databinding.ActivityLoginBinding
import test.vtd.appointment_app.model.Users
import test.vtd.appointment_app.sharedPreferences.mySharedPreferences
import test.vtd.appointment_app.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        getControls()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.status.observe(this){ status ->
            when(status){
                "success" -> {
                    Toast.makeText(this, status, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SplashActivity::class.java)
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
            tvdangky.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
            btndangnhap.setOnClickListener {
                val strEmail:String = emailDn.text.toString().trim()
                val strPass:String = passDn.text.toString().trim()
                if (TextUtils.isEmpty(strEmail)) {
                    Toast.makeText(applicationContext, "Please enter email", Toast.LENGTH_SHORT)
                        .show()
                } else if (TextUtils.isEmpty(strPass)) {
                    Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Paper.book().write("email", strEmail)
                    Paper.book().write("pass", strPass)
                    val user = Users()
                    user.email = strEmail
                    user.password = strPass
                    mySharedPreferences.saveUser(this@LoginActivity, user)
                    viewModel.login(this@LoginActivity, strEmail, strPass)
                }
            }
            txtresetpass.setOnClickListener {
                val intent = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mySharedPreferences.getUser(this) != null){
            val strEmail = mySharedPreferences.getUser(this)!!.email
            binding.emailDn.setText(strEmail)
            if (mySharedPreferences.getIsLogin(applicationContext)) {
                val strPass = mySharedPreferences.getUser(this)!!.password
                binding.passDn.setText(strPass)
                viewModel.login(this, strEmail, strPass)
            }
        }
    }
}