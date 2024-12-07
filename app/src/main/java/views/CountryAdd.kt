package views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.traveldiary.databinding.CountryAddBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding
import com.example.traveldiary.models.Country
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryAdd : AppCompatActivity() {

    private lateinit var binding: CountryAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CountryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Add Country"


        // Handle menu button click to open the drawer
        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }


        val continents = listOf(
            "Africa",
            "Antarctica",
            "Asia",
            "Europe",
            "North America",
            "Australia",
            "South America"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, continents)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerContinent.adapter = adapter

        // Set a listener to handle item selection



        toolbarBinding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSave.setOnClickListener {
            val countryName = binding.editTextCountryName.text.toString()
            val selectedContinent = binding.spinnerContinent.selectedItem.toString()

            if (countryName.isEmpty() || selectedContinent == "Select Continent") {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Vytvoření objektu Country bez ID
            val newCountry = Country(name = countryName, continent = selectedContinent)

            // Send data to server
            RetrofitClient.instance.saveCountry(newCountry).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CountryAdd,
                            "Country saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Log the error for debugging
                        Log.e("RetrofitError", "Response code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                        Toast.makeText(
                            this@CountryAdd,
                            "Failed to save country: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@CountryAdd,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            val intent = Intent(this, CountryList::class.java)
            startActivity(intent)
        }
    }
}