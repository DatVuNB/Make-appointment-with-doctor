package test.vtd.appointment_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import test.vtd.appointment_app.R
import android.view.animation.AlphaAnimation
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import test.vtd.appointment_app.adapter.NearDoctorAdapter
import test.vtd.appointment_app.databinding.ActivityMainBinding
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private lateinit var binding: ActivityMainBinding
    private val adapter = NearDoctorAdapter(mutableListOf())
    private val handler = Handler(Looper.getMainLooper())
    private val displayInterval = 10_000L
    private var doctorsList: List<Doctors> = emptyList()
    private var currentDoctorIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initNearDoctor()
        actionMenu()
        setupSearch()
    }

    private fun actionMenu() {
        binding.chipNavigationBar.setItemSelected(R.id.menu_home, true)
        binding.chipNavigationBar.setOnItemSelectedListener {id ->
            when(id){
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_favorite -> {
                    val intent = Intent(this, FavoriteActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_bookmark -> {
                    val intent = Intent(this, BookMarkActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    private fun initNearDoctor() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            viewModel.loadDoctors().observe(this@MainActivity) { doctors ->
                doctorsList = doctors
                if (doctorsList.isNotEmpty()) {
                    startDoctorRotation()
                }
                progressBar.visibility = View.GONE
                adapter.updateData(doctors)
            }
        }
    }

    private fun startDoctorRotation() {
        updateDoctorInfo()

        handler.postDelayed(object : Runnable {
            override fun run() {
                currentDoctorIndex = (currentDoctorIndex + 1) % doctorsList.size
                updateDoctorInfo()
                handler.postDelayed(this, displayInterval)
            }
        }, displayInterval)
    }

    private fun updateDoctorInfo() {
        val doctor = doctorsList[currentDoctorIndex]

        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply { duration = 500 }
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply { duration = 500 }

        binding.apply {
            imgPerson.startAnimation(fadeOut)
            tvName.startAnimation(fadeOut)
            tvSpecial.startAnimation(fadeOut)
            tvTime.startAnimation(fadeOut)
            tvDate.startAnimation(fadeOut)

            handler.postDelayed({
                Glide.with(this@MainActivity)
                    .load(doctor.picture)
                    .into(imgPerson)

                tvName.text = doctor.name
                tvSpecial.text = doctor.special
                tvTime.text = doctor.time
                tvDate.text = doctor.date

                imgPerson.startAnimation(fadeIn)
                tvName.startAnimation(fadeIn)
                tvSpecial.startAnimation(fadeIn)
                tvTime.startAnimation(fadeIn)
                tvDate.startAnimation(fadeIn)
            }, 500)
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterDoctors(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterDoctors(newText)
                return true
            }
        })
    }

    private fun filterDoctors(query: String?) {
        viewModel.filterDoctors(query!!).observe(this@MainActivity){ doctors ->
            adapter.updateData(doctors)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.chipNavigationBar.setItemSelected(R.id.menu_home, true)
        handler.removeCallbacksAndMessages(null)
    }
}
