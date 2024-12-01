package com.example.traveldiary

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.traveldiary.databinding.CountryListBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding

class CountryList : AppCompatActivity() {

    private lateinit var binding: CountryListBinding
    //private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //databaseHelper = DatabaseHelper()

        val toolbarBinding = ToolbarBinding.bind(binding.mytoolbar.root)
        toolbarBinding.toolbarTitle.text = "Countries"

        // Handle menu button click to open the drawer
        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayoutCountries.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayoutCountries.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayoutCountries.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }

        toolbarBinding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}