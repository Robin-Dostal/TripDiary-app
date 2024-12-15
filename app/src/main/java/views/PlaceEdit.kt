package views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.traveldiary.adapters.CountrySpinnerAdapter
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.PlaceAddBinding
import com.example.traveldiary.databinding.ToolbarBinding
import com.example.traveldiary.models.Country
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class PlaceEdit : AppCompatActivity() {

    private lateinit var binding: PlaceAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PlaceAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        menu()
        fetchCountries()

        val id = intent.getStringExtra("id") ?: ""
        val placeName = intent.getStringExtra("name") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val formattedDate = intent.getStringExtra("formattedDate") ?: ""
        val comment = intent.getStringExtra("comment") ?: ""
        val countryId = intent.getStringExtra("countryId") ?: ""
        val countryName = intent.getStringExtra("countryName") ?: ""
        val countryContinent = intent.getStringExtra("countryContinent") ?: ""


        binding.textPlaceName.setText(placeName)
        binding.textViewSelectedDate.setText(formattedDate)
        binding.textNotes.setText(comment)

        // Delete button logic
        binding.buttonDelete.setOnClickListener {
            // Validate that the country ID is available
            if (id.isNullOrEmpty()) {
                Toast.makeText(this, "Place ID is missing. Cannot delete.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Confirm the deletion action (optional)
            AlertDialog.Builder(this).apply {
                setTitle("Delete Place")
                setMessage("Are you sure you want to delete this place?")
                setPositiveButton("Yes") { _, _ ->
                    // Make API call to delete the country
                    RetrofitClient.instance.deletePlace(id).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@PlaceEdit, "Place deleted successfully", Toast.LENGTH_SHORT).show()
                                // Return to the calling activity after deletion
                                setResult(RESULT_OK) // Optionally, pass additional data if needed
                                val intent = Intent(this@PlaceEdit, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.e("PlaceEdit", "Failed to delete place. Response code: ${response.code()}")
                                Log.e("PlaceEdit", "Error: ${response.errorBody()?.string()}")
                                Toast.makeText(this@PlaceEdit, "Failed to delete place", Toast.LENGTH_SHORT).show()
                            }

                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("PlaceEdit", "Error: ${t.message}")
                            Toast.makeText(this@PlaceEdit, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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


    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.toolbar.root)
        toolbarBinding.toolbarTitle.text = "Edit place"

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
            val intent = Intent(this, PlaceEdit::class.java)
            startActivity(intent)
        }

        drawerLayoutBinding.menuItem2.setOnClickListener{
            val intent = Intent(this, CountryList::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCountries() {
        val api = RetrofitClient.instance
        val countryId = intent.getStringExtra("countryId") ?: ""
        val countryName = intent.getStringExtra("countryName") ?: ""

        api.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(
                call: Call<List<Country>>,
                response: Response<List<Country>>
            ) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Log.e("CountryList", "Fetched countries: $countries")

                    // Populate Spinner with the fetched countries
                    val spinnerAdapter = CountrySpinnerAdapter(this@PlaceEdit, countries)
                    binding.spinnerContinent.adapter = spinnerAdapter

                    // Find the index of the selected country
                    val selectedIndex = countries.indexOfFirst { it._id == countryId }

                    // Set the selection if a match is found
                    if (selectedIndex >= 0) {
                        binding.spinnerContinent.setSelection(selectedIndex)
                    } else {
                        Log.w("CountryList", "Country with ID $countryId not found")
                    }
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedDay}/${selectedMonth + 1}/${selectedYear}"
                binding.textViewSelectedDate.text = "Selected Date: $formattedDate"
                Toast.makeText(this, "Date selected: $formattedDate", Toast.LENGTH_SHORT).show()
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}