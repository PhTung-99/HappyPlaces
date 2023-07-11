package com.learn.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.learn.happyplaces.adapters.HappyPlacesAdapter
import com.learn.happyplaces.database.DatabaseHandler
import com.learn.happyplaces.databinding.ActivityMainBinding
import com.learn.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val getListFromAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        Log.e("MAIN", result.resultCode.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            getHappyPlacesListFromLocalDB()
        }
    }

    companion object {
        const val EXTRA_PLACE_DETAILS = "extra_place_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddHappyPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
            getListFromAdd.launch(intent)
        }

        getHappyPlacesListFromLocalDB()
    }

    private fun getHappyPlacesListFromLocalDB() {
        val dbHandler = DatabaseHandler(this)
        val getHappyPlacesList = dbHandler.getHappyPlacesList()
        if (getHappyPlacesList.size > 0) {
            binding.rvHappyPlaces.visibility = View.VISIBLE
            binding.tvNoRecord.visibility = View.GONE
            setupHappyPlacesRecycleView(getHappyPlacesList)
        } else {
            binding.rvHappyPlaces.visibility = View.GONE
            binding.tvNoRecord.visibility = View.VISIBLE
        }
    }

    private fun setupHappyPlacesRecycleView(happyPlaces: ArrayList<HappyPlaceModel>) {
        binding.rvHappyPlaces.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = HappyPlacesAdapter(happyPlaces)
        binding.rvHappyPlaces.setHasFixedSize(true)
        binding.rvHappyPlaces.adapter = adapter

        adapter.setOnClickListener(object:HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })
    }
}