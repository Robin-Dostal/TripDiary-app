package views

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveldiary.adapters.PlacesAdapter
import com.example.traveldiary.databinding.ActivityMainBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding
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


        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Home"

        menu()
        fetchPlaces()


        // Handle menu button click to open the drawer

        // Handle menu item clicks (optional)
        /*
        binding.drawerMenu.findViewById<View>(R.id.menuItem1).setOnClickListener {
            // Perform action for Menu Item 1
            drawerLayout.closeDrawer(Gravity.END)
        }
         */

        // Initialize the Spinner with some sample data
        val filterOptions = arrayOf("Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.filterDropdown.adapter = adapter // Use binding to access the Spinner

        // Initialize the Search Bar
        binding.searchBar.setOnClickListener {
            // You can add functionality for the search bar here
        }


        // Sample data
        /*
        val places = listOf(
            Place("Eiffel Tower", "France / Europe", "2024-01-10"),
            Place("Great Wall", "China / Asia", "2023-08-15"),
            Place("Grand Canyon", "USA / North America", "2023-06-05"),
            Place("Eiffel Tower", "France / Europe", "2024-01-10"),
            Place("Great Wall", "China / Asia", "2023-08-15"),
            Place("Grand Canyon", "USA / North America", "2023-06-05"),
            Place("Eiffel Tower", "France / Europe", "2024-01-10"),
            Place("Great Wall", "China / Asia", "2023-08-15"),
            Place("Grand Canyon", "USA / North America", "2023-06-05"),
            Place("Eiffel Tower", "France / Europe", "2024-01-10"),
            Place("Great Wall", "China / Asia", "2023-08-15"),
            Place("Grand Canyon", "USA / North America", "2023-06-05"),
            Place("Eiffel Tower", "France / Europe", "2024-01-10"),
            Place("Great Wall", "China / Asia", "2023-08-15"),
            Place("Kokokokook", "USA / North America", "2023-06-05")
        )

         */

        // Initialize RecyclerView
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(this)

        //binding.placesRecyclerView.adapter = PlacesAdapter(places)

        // Button click listener
        binding.addPlaceButton.setOnClickListener {
            // Add logic for adding a new place
        }
    }

    private fun fetchPlaces() {
        val api = RetrofitClient.instance

        api.getPlaces().enqueue(object : Callback<List<Place>> {
            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                if (response.isSuccessful) {
                    val places = response.body() ?: emptyList()
                    Log.e("PlaceList", "Fetched places: $places")

                    // Set up RecyclerView with the data
                    binding.placesRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.placesRecyclerView.adapter = PlacesAdapter(places) {  /* country ->

                        val intent = Intent(this@MainActivity, CountryEdit::class.java).apply {
                            Log.e("country id", "$country")
                            putExtra("id", country._id)
                            putExtra("name", country.name)
                            putExtra("continent", country.continent)
                            startActivityForResult(intent, CountryList.EDIT_COUNTRY_REQUEST_CODE) // Single call
                        }
                        startActivity(intent)

                         */
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch countries", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Countries"

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