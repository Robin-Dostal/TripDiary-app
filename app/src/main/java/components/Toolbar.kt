package components

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.traveldiary.databinding.ToolbarBinding
import views.MainActivity

abstract class Toolbar<T : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: T
    private lateinit var toolbarBinding: ToolbarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarBinding = ToolbarBinding.inflate(layoutInflater)
        binding = getViewBinding()
        setContentView(toolbarBinding.root)
        toolbarBinding.toolbar.addView(binding.root)
        setupToolbar()
    }
    protected abstract fun getViewBinding(): T
    private fun setupToolbar() {
        toolbarBinding.logo.setOnClickListener {
            Log.e("hahahha","logogogo")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}