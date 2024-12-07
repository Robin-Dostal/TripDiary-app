package com.example.traveldiary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.traveldiary.databinding.ItemPlaceBinding
import com.example.traveldiary.models.Country

data class Country(val name: String, val continent: String)

class CountryAdapter(
    private val countries: List<com.example.traveldiary.models.Country>,
    private val onItemClick: (Country) -> Unit) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.placeNameTextView.text = country.name
            binding.placeLocationTextView.text = country.continent
            binding.visitDateTextView.text = ""
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding =
            ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.bind(countries[position])

        // Set click listener for the root view of the item
        holder.binding.root.setOnClickListener {
            onItemClick(country)
        }
    }

    override fun getItemCount(): Int = countries.size
}
