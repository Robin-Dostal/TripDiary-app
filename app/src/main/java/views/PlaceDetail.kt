package views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.traveldiary.databinding.DrawerMenuBinding
import com.example.traveldiary.databinding.PlaceDetailBinding
import com.example.traveldiary.databinding.ToolbarBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlaceDetail : AppCompatActivity() {

    private lateinit var binding: PlaceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menu()

        val placeName = intent.getStringExtra("name") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val comment = intent.getStringExtra("comment") ?: ""
        val countryName = intent.getStringExtra("countryName") ?: ""
        val countryContinent = intent.getStringExtra("countryContinent") ?: ""

        //preformatovani data
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val dateForm = inputFormat.parse(date)
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = outputFormat.format(dateForm)


        binding.textPlaceName.setText(placeName)
        binding.textCountryName.setText("${countryName} | ${countryContinent}")
        binding.textViewSelectedDate.setText(formattedDate)
        binding.textNotes.setText(comment)

        binding.buttonEdit.setOnClickListener {
            val intent = Intent(this, PlaceEdit::class.java)
            startActivity(intent)
        }
    }

    private fun menu() {
        val toolbarBinding = ToolbarBinding.bind(binding.toolbar.root)
        toolbarBinding.toolbarTitle.text = "Place detail"

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