package com.learn.happyplaces.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learn.happyplaces.databinding.ActivityHappyPlaceDetailBinding
import com.learn.happyplaces.models.HappyPlaceModel

class HappyPlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHappyPlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHappyPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var happyPlaceDetailModel: HappyPlaceModel? = null
        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            happyPlaceDetailModel = intent.getParcelableExtra<HappyPlaceModel>(MainActivity.EXTRA_PLACE_DETAILS)
        }

        happyPlaceDetailModel?.let {
            setSupportActionBar(binding.toolbarHappyPlaceDetail)
            supportActionBar?.let { actionBar -> {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.title = it.title
                }
            }

            binding.toolbarHappyPlaceDetail.setNavigationOnClickListener {
                onBackPressed()
            }

            binding.ivPlaceImage.setImageURI(Uri.parse(it.image))
            binding.tvDescription.text = it.description
            binding.tvLocation.text = it.location
        }
    }
}