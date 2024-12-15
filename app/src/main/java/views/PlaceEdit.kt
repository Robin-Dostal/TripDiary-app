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
import com.example.traveldiary.models.Place
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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

            val updatedPlace = Place(
                _id = id,
                name = placeName,
                date = formattedDate, // ISO-8601 formatted date
                comment = notes,
                countryId = selectedCountry?._id ?: ""

            )

            Log.d("PlaceEdit", "Updated Place: $updatedPlace")

            // Send the updated data to the server using the updatePlace API
            RetrofitClient.instance.updatePlace(updatedPlace).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PlaceEdit, "Place updated successfully", Toast.LENGTH_SHORT).show()

                        // Optionally, return to the previous screen or main activity
                        setResult(RESULT_OK)  // Optionally pass additional data if needed
                        val intent = Intent(this@PlaceEdit, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("PlaceEdit", "Failed to update place. Response code: ${response.code()}")
                        Log.e("PlaceEdit", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(this@PlaceEdit, "Failed to update place", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("PlaceEdit", "Error: ${t.message}")
                    Toast.makeText(this@PlaceEdit, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
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