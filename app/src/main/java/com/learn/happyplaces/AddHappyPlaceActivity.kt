package com.learn.happyplaces

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.learn.happyplaces.databinding.ActivityAddHappyPlaceBinding

class AddHappyPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddHappyPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)

//
//        setupActionB

        setContentView(binding.root)


    }
}