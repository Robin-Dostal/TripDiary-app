package views

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldiary.databinding.CountryAddBinding
import com.example.traveldiary.models.Country
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryEdit : AppCompatActivity() {

    private lateinit var binding: CountryAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CountryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val continents = listOf(
            "Africa",
            "Antarctica",
            "Asia",
            "Europe",
            "North America",
            "Australia",
            "South America"
        )

        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, continents)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerContinent.adapter = adapter

        // Get the country data passed through intent
        //id = intent.getStringExtra("id") ?: ""
        val countryId = intent.getStringExtra("id") ?: ""
        val countryName = intent.getStringExtra("name") ?: ""
        val continent = intent.getStringExtra("continent") ?: ""

        // Populate fields with current country data
        binding.editTextCountryName.setText(countryName)
        val continentIndex = continents.indexOf(continent)
        if (continentIndex >= 0) {
            binding.spinnerContinent.setSelection(continentIndex)
        }

        // Save button logic
        binding.buttonSave.setOnClickListener {
            val updatedName = binding.editTextCountryName.text.toString().trim()
            val updatedContinent = binding.spinnerContinent.selectedItem.toString()

            // Validate input
            if (updatedName.isEmpty() || updatedContinent == "Select Continent") {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve the country ID passed to this activity
            if (countryId.isNullOrEmpty()) {
                Toast.makeText(this, "Country ID is missing. Cannot update.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare updated country data
            val updatedCountry = Country(
                _id = countryId, // Use the ID retrieved from the intent
                name = updatedName,
                continent = updatedContinent
            )

            // Make API call to update the country on the server
            RetrofitClient.instance.updateCountry(updatedCountry).enqueue(object :
                Callback<Void> { // Assuming your API returns a Void response
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Log.e("Response", "$response" )
                    if (response.isSuccessful) {
                        Toast.makeText(this@CountryEdit, "Country updated successfully", Toast.LENGTH_SHORT).show()

                        // Return the updated country data to the calling activity
                        val resultIntent = Intent().apply {
                            //putExtra("id", countryId)
                            putExtra("updatedName", updatedName)
                            putExtra("updatedContinent", updatedContinent)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish() // Close the edit activity
                    } else {
                        Log.e("CountryEdit", "Failed to update country. Response code: ${response.code()}")
                        Log.e("CountryEdit", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@CountryEdit, "Failed to update country", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@CountryEdit, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // Delete button logic
        binding.buttonDelete.setOnClickListener {
            // Validate that the country ID is available
            if (countryId.isNullOrEmpty()) {
                Toast.makeText(this, "Country ID is missing. Cannot delete.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Confirm the deletion action (optional)
            AlertDialog.Builder(this).apply {
                setTitle("Delete Country")
                setMessage("Are you sure you want to delete this country?")
                setPositiveButton("Yes") { _, _ ->
                    // Make API call to delete the country
                    RetrofitClient.instance.deleteCountry(countryId).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@CountryEdit, "Country deleted successfully", Toast.LENGTH_SHORT).show()

                                // Return to the calling activity after deletion
                                setResult(RESULT_OK) // Optionally, pass additional data if needed
                                finish() // Close the edit activity
                            } else {
                                Log.e("CountryEdit", "Failed to delete country. Response code: ${response.code()}")
                                Log.e("CountryEdit", "Error: ${response.errorBody()?.string()}")
                                Toast.makeText(this@CountryEdit, "Failed to delete country", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("CountryEdit", "Error: ${t.message}")
                            Toast.makeText(this@CountryEdit, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss() // Close the dialog if the user cancels
                }
                create()
                show()
            }
        }

    }
}
