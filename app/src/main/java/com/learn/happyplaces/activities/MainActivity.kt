package com.learn.happyplaces.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.learn.happyplaces.adapters.HappyPlacesAdapter
import com.learn.happyplaces.database.DatabaseHandler
import com.learn.happyplaces.databinding.ActivityMainBinding
import com.learn.happyplaces.models.HappyPlaceModel
import com.learn.happyplaces.utils.SwipeToDeleteCallback
import com.learn.happyplaces.utils.SwipeToEditCallback

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
        val adapter = HappyPlacesAdapter(happyPlaces, this)
        binding.rvHappyPlaces.setHasFixedSize(true)
        binding.rvHappyPlaces.adapter = adapter

        adapter.setOnClickListener(object:HappyPlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.notifyEditItem(
                    viewHolder.layoutPosition
                ) {
                    val intent = Intent(this@MainActivity, AddHappyPlaceActivity::class.java)
                    intent.putExtra(EXTRA_PLACE_DETAILS, it)
                    startActivity(intent)
                }
            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding.rvHappyPlaces)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val happyPlaceAdapter = binding.rvHappyPlaces.adapter as HappyPlacesAdapter
                happyPlaceAdapter.removeAt(viewHolder.adapterPosition)

                getHappyPlacesListFromLocalDB()
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvHappyPlaces)

    }
}