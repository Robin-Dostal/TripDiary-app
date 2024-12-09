package views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.traveldiary.adapters.CountryAdapter
import com.example.traveldiary.databinding.CountryListBinding
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.ToolbarBinding
import com.example.traveldiary.models.Country
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CountryList : AppCompatActivity() {

    companion object {
        private const val EDIT_COUNTRY_REQUEST_CODE = 1
    }

    private lateinit var binding: CountryListBinding
    //private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = CountryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu()
        fetchCountries()

        binding.addPlaceButton.setOnClickListener{
            val intent = Intent(this, CountryAdd::class.java)
            startActivity(intent)
        }

        /*
        val api = RetrofitClient.instance

        // Fetch countries
        api.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Log.e("CountryList", "Fetched countries: $countries")

                    // Set up RecyclerView with data
                    binding.placesRecyclerView.layoutManager = LinearLayoutManager(this@CountryList)
                    binding.placesRecyclerView.adapter = CountryAdapter(countries) { country ->
                        val intent = Intent(this@CountryList, CountryEdit::class.java)
                        intent.putExtra("name", country.name)
                        intent.putExtra("continent", country.continent)
                        startActivityForResult(intent, EDIT_COUNTRY_REQUEST_CODE)
                        startActivity(intent)
                    }


                } else {
                    Toast.makeText(this@CountryList, "Failed to fetch countries", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@CountryList, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        // Handle menu button click to open the drawer
        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayoutCountries.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayoutCountries.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayoutCountries.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }

        binding.addPlaceButton.setOnClickListener{
            val intent = Intent(this, CountryAdd::class.java)
            startActivity(intent)
        }



        toolbarBinding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

         */
    }

    private fun fetchCountries() {
        val api = RetrofitClient.instance

        api.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(call: Call<List<Country>>, response: Response<List<Country>>) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Log.e("CountryList", "Fetched countries: $countries")

                    // Set up RecyclerView with the data
                    binding.placesRecyclerView.layoutManager = LinearLayoutManager(this@CountryList)
                    binding.placesRecyclerView.adapter = CountryAdapter(countries) { country ->
                        val intent = Intent(this@CountryList, CountryEdit::class.java).apply {
                            Log.e("country id", "$country")
                            putExtra("id", country._id)
                            putExtra("name", country.name)
                            putExtra("continent", country.continent)
                            startActivityForResult(intent, EDIT_COUNTRY_REQUEST_CODE) // Single call
                        }
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@CountryList, "Failed to fetch countries", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Country>>, t: Throwable) {
                Toast.makeText(this@CountryList, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.toolbar.root)
        toolbarBinding.toolbarTitle.text = "Countries"

        toolbarBinding.logo.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val drawerLayoutBinding = DrawerMenuBinding.bind(binding.mydrawerMenu.root)
        toolbarBinding.menu.setOnClickListener{
            if (binding.drawerLayoutCountries.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayoutCountries.closeDrawer(GravityCompat.END) // Close the drawer if open
            } else {
                binding.drawerLayoutCountries.openDrawer(GravityCompat.END) // Open the drawer if closed
            }
        }

        drawerLayoutBinding.menuItem2.setOnClickListener{
            val intent = Intent(this, CountryList::class.java)
            startActivity(intent)
        }
    }
}