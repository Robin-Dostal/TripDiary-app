package com.example.traveldiary.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldiary.databinding.ItemPlaceBinding
import com.example.traveldiary.models.Place
import java.text.SimpleDateFormat
import java.util.Locale

data class Places(val name: String, val countryId: String, val date: String)

class PlacesAdapter(
    private val places: List<com.example.traveldiary.models.Place>,
    private val onItemClick: (Place) -> Unit) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SuspiciousIndentation")
        fun bind(place: Place) {
            binding.placeNameTextView.text = place.name
            binding.placeLocationTextView.text = "${place.country?.name} | ${place.country?.continent}"

            val isoDate = place.date
                try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(isoDate)

                // Format the Date object into a readable string
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = outputFormat.format(date)

                // Set the formatted date to the TextView
                binding.visitDateTextView.text = formattedDate
                //binding.visitDateTextView.text = place.date
                } catch (e: Exception) {
                    e.printStackTrace()
                    binding.visitDateTextView.text = "Invalid Date" // Fallback in case of an error
                }

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