package test.vtd.appointment_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import test.vtd.appointment_app.adapter.FavoriteAdapter
import test.vtd.appointment_app.databinding.ActivityFavoriteBinding
import test.vtd.appointment_app.model.Doctors
import test.vtd.appointment_app.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.IClickListener {
    private val viewModel = FavoriteViewModel()
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initFavoriteDoctor()
    }

    private fun initFavoriteDoctor() {
        viewModel.loadData().observe(this@FavoriteActivity){ doctor ->
            adapter.updateData(doctor)
        }
    }

    private fun initRecyclerView() {
        adapter = FavoriteAdapter(this)
        binding.rcvFavorite.layoutManager =
            LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
        binding.rcvFavorite.adapter = adapter
    }

    override fun unFavorite(doctor: Doctors) {
        viewModel.unFavorite(doctor)
    }

    override fun onDoctorClick(doctor: Doctors) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra("object", doctor)
        startActivity(intent)
    }
}