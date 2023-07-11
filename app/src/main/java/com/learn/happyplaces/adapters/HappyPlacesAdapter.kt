package com.learn.happyplaces.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learn.happyplaces.databinding.ItemHappyPlaceBinding
import com.learn.happyplaces.models.HappyPlaceModel

class HappyPlacesAdapter(private val happyPlaces: ArrayList<HappyPlaceModel>): RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class ViewHolder(private val binding: ItemHappyPlaceBinding): RecyclerView.ViewHolder(binding.root) {
        val ivPlaceImage = binding.ivPlaceImage
        val tvTitle = binding.tvTitle
        val tvDescription = binding.tvDescription
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHappyPlaceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return happyPlaces.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val happyPlace = happyPlaces[position]
        holder.tvTitle.text = happyPlace.title
        holder.tvDescription.text = happyPlace.description
        holder.ivPlaceImage.setImageURI(Uri.parse(happyPlace.image))
        holder.root.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, happyPlace)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: HappyPlaceModel)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}