package com.example.traveldiary

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.traveldiary.databinding.ActivityMainBinding
import com.example.traveldiary.databinding.ToolbarBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Home"


        // Initialize the Spinner with some sample data
        val filterOptions = arrayOf("Option 1", "Option 2", "Option 3")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.filterDropdown.adapter = adapter // Use binding to access the Spinner

        // Initialize the Search Bar
        binding.searchBar.setOnClickListener {
            // You can add functionality for the search bar here
        }
    }
}