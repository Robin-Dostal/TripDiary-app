package com.example.traveldiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldiary.databinding.ItemPlaceBinding
import com.example.traveldiary.models.Place

data class Places(val name: String, val countryId: String, val date: String)

class PlacesAdapter(
    private val places: List<com.example.traveldiary.models.Place>,
    private val onItemClick: (Place) -> Unit) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(place: Place) {
            binding.placeNameTextView.text = place.name
            binding.placeLocationTextView.text = place.countryId
            binding.visitDateTextView.text = place.date
         }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(places[position])

        // Set click listener for the root view of the item
        holder.binding.root.setOnClickListener {
            onItemClick(place)
        }
    }

    override fun getItemCount(): Int = places.size
}