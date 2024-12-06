package views

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldiary.databinding.CountryEditBinding

class CountryEdit : AppCompatActivity() {

    private lateinit var binding: CountryEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountryEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example continents
        val continents = listOf("Africa", "Asia", "Europe", "North America", "Oceania", "South America")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, continents)
        binding.editContinentSpinner.adapter = adapter

        // Fetch country details passed via Intent
        val countryName = intent.getStringExtra("countryName") ?: ""
        val countryContinent = intent.getStringExtra("countryContinent") ?: ""

        // Populate fields
        binding.editCountryName.setText(countryName)
        val continentIndex = continents.indexOf(countryContinent)
        if (continentIndex >= 0) {
            binding.editContinentSpinner.setSelection(continentIndex)
        }

        // Save button click listener
        binding.saveButton.setOnClickListener {
            val updatedName = binding.editCountryName.text.toString()
            val updatedContinent = binding.editContinentSpinner.selectedItem.toString()

            if (updatedName.isEmpty()) {
                Toast.makeText(this, "Country name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send updated details back
            Toast.makeText(this, "Updated $updatedName in $updatedContinent", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Delete button click listener
        binding.deleteButton.setOnClickListener {
            // Handle delete logic here
            Toast.makeText(this, "Deleted $countryName", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
