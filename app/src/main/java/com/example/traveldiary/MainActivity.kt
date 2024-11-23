package com.example.traveldiary

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveldiary.databinding.ActivityMainBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding


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


        // Handle menu button click to open the drawer
        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }

        // Handle menu item clicks (optional)
        /*
        binding.drawerMenu.findViewById<View>(R.id.menuItem1).setOnClickListener {
            // Perform action for Menu Item 1
            drawerLayout.closeDrawer(Gravity.END)
        }
         */
        drawerLayoutBinding.menuItem2.setOnClickListener{
            val intent = Intent(this, CountryList::class.java)
            startActivity(intent)
        }



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

        // Initialize RecyclerView
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.placesRecyclerView.adapter = PlacesAdapter(places)

        // Button click listener
        binding.addPlaceButton.setOnClickListener {
            // Add logic for adding a new place
        }
    }
}