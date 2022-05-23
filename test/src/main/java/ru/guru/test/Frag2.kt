package ru.guru.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.guru.test.databinding.ActivityFrag2Binding

class Frag2 : AppCompatActivity() {

    private lateinit var binding: ActivityFrag2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrag2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            navBar.selectedItemId = R.id.page_2

            navBar.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.main -> {
                        val intent = Intent(this@Frag2, MainActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_1 -> {
                        val intent = Intent(this@Frag2, Frag1::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.page_2 -> {
                        Toast.makeText(this@Frag2, "Эй", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.page_3 -> {
                        val intent = Intent(this@Frag2, Frag3::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> {false}
                }
            }
        }
    }
}