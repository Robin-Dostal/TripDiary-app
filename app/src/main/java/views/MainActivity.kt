package views

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveldiary.adapters.PlacesAdapter
import com.example.traveldiary.databinding.ActivityMainBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding
import com.example.traveldiary.models.Country
import com.example.traveldiary.models.Place
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu()
        setupSearchBar()
        fetchCountries()
        fetchPlaces()



        // Initialize the Search Bar
        binding.searchBar.setOnClickListener {
            // You can add functionality for the search bar here
        }

        // Initialize RecyclerView
        //binding.placesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Button click listener
        binding.addPlaceButton.setOnClickListener {
            val intent = Intent(this, PlaceAdd::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPlaces() {
        val api = RetrofitClient.instance

        api.getPlaces().enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                if (response.isSuccessful) {
                    allPlaces = response.body() ?: emptyList() // Save the full list
                    setupRecyclerView(allPlaces) // Initially show all places
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch places", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterPlaces(query: String) {
        val filteredPlaces = if (query.isEmpty()) {
            allPlaces // Show all places if the query is empty
        } else {
            allPlaces.filter { place ->
                place.name.contains(query, ignoreCase = true) // Case-insensitive filtering
            }
        }
        setupRecyclerView(filteredPlaces) // Update RecyclerView with the filtered list
    }

    private fun setupRecyclerView(places: List<Place>) {
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placesRecyclerView.adapter = PlacesAdapter(places) { place ->
            val intent = Intent(this, PlaceDetail::class.java).apply {
                putExtra("id", place._id)
                putExtra("name", place.name)
                putExtra("date", place.date)
                putExtra("comment", place.comment)
                putExtra("countryName", place.country?.name)
                putExtra("countryContinent", place.country?.continent)
                putExtra("countryId", place.country?._id)
            }
            startActivity(intent)
        }
    }


    private fun fetchCountries() {
        val api = RetrofitClient.instance

        api.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(
                call: Call<List<Country>>,
                response: Response<List<Country>>
            ) {
                if (response.isSuccessful) {
                    val countries = mutableListOf(Country(name = "All countries", continent = "")) // Add placeholder
                    countries.addAll(response.body() ?: emptyList()) // Add the fetched countries
                    Log.e("CountryList", "Fetched countries: $countries")

                    // Populate Spinner with the fetched countries
                    //binding.filterDropdown.adapter = CountrySpinnerAdapter(this@MainActivity, countries)
                } else {
                    Log.e(
                        "CountryList",
                        "Failed to fetch countries. Response code: ${response.code()}"
                    )
                }
            }
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Log.e("CountryList", "Error fetching countries: ${t.message}")
            }
        })
    }

    // Store the original list of places
    private var allPlaces: List<Place> = emptyList()
    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                filterPlaces(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Home"

        toolbarBinding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }

        drawerLayoutBinding.menuItem1.setOnClickListener{
            val intent = Intent(this, PlaceAdd::class.java)
            startActivity(intent)
        }

        drawerLayoutBinding.menuItem2.setOnClickListener{
            val intent = Intent(this, CountryList::class.java)
            startActivity(intent)
        }
    }
}