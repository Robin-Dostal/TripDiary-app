package views

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.traveldiary.adapters.CountrySpinnerAdapter
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.PlaceAddBinding
import com.example.traveldiary.databinding.ToolbarBinding
import com.example.traveldiary.models.Country
import com.example.traveldiary.models.Place
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class PlaceAdd : AppCompatActivity() {

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

        binding.buttonSave.setOnClickListener {
            val placeName = binding.textPlaceName.text.toString()
            val selectedDateText = binding.textViewSelectedDate.text.toString().replace("Selected Date: ", "")
            val notes = binding.textNotes.text.toString()

            val selectedCountry = binding.spinnerContinent.selectedItem as? Country

            // Validate inputs
            if (placeName.isEmpty() || selectedDateText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formattedDate = try {
                // Use the correct pattern to match your date format
                val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
                val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault()) // ISO format
                val parsedDate = LocalDate.parse(selectedDateText, inputFormatter)
                // Convert to ISO 8601 format
                outputFormatter.format(parsedDate)
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create the Place object
            val newPlace = Place(
                name = placeName,
                date = formattedDate, // ISO-8601 formatted date
                comment = notes,
                countryId = selectedCountry?._id ?: ""

            )
            Log.d("Place","$newPlace")
            // Send data to server
            RetrofitClient.instance.savePlace(newPlace).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@PlaceAdd,
                            "Place saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Redirect to another activity if needed
                        val intent = Intent(this@PlaceAdd, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        // Log the error for debugging
                        Log.e("RetrofitError", "Response code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                        Toast.makeText(
                            this@PlaceAdd,
                            "Failed to save place: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        this@PlaceAdd,
                        "Error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }


    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.toolbar.root)
        toolbarBinding.toolbarTitle.text = "Add new place"

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

    private fun fetchCountries() {
        val api = RetrofitClient.instance

        api.getCountries().enqueue(object : Callback<List<Country>> {
            override fun onResponse(
                call: Call<List<Country>>,
                response: Response<List<Country>>
            ) {
                if (response.isSuccessful) {
                    val countries = response.body() ?: emptyList()
                    Log.e("CountryList", "Fetched countries: $countries")

                    // Populate Spinner with the fetched countries
                    binding.spinnerContinent.adapter =
                        CountrySpinnerAdapter(this@PlaceAdd, countries)

                    // Optionally handle default selection
                    if (countries.isNotEmpty()) {
                        binding.spinnerContinent.setSelection(0) // Select the first country by default
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
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDate = "${formattedDay}/${formattedMonth}/${selectedYear}"
                binding.textViewSelectedDate.text = "Selected Date: $formattedDate"
                Toast.makeText(this, "Date selected: $formattedDate", Toast.LENGTH_SHORT).show()
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}