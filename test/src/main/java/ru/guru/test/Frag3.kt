package ru.guru.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.guru.test.databinding.ActivityFrag3Binding

class Frag3 : AppCompatActivity() {

    private lateinit var binding: ActivityFrag3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrag3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navBar.selectedItemId = R.id.page_3

            navBar.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.main -> {
                        val intent = Intent(this@Frag3, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_1 -> {
                        val intent = Intent(this@Frag3, Frag1::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_2 -> {
                        val intent = Intent(this@Frag3, Frag2::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_3 -> {
                        Toast.makeText(this@Frag3, "Эй", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {false}
                }
            }
        }
    }
}