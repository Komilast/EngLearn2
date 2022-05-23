package ru.guru.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.guru.test.databinding.ActivityFrag1Binding

class Frag1 : AppCompatActivity() {

    private lateinit var binding: ActivityFrag1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrag1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navBar.selectedItemId = R.id.page_1

            navBar.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.main -> {
                        val intent = Intent(this@Frag1, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_1 -> {
                        Toast.makeText(this@Frag1, "Эй", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.page_2 -> {
                        val intent = Intent(this@Frag1, Frag2::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_3 -> {
                        val intent = Intent(this@Frag1, Frag3::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> {false}
                }
            }
        }
    }
}