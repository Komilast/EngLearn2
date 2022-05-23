package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Fragments.*
import ru.guru.englearn2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            bnb.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.bnb_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, MainFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.bnb_library -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, LibraryFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.bnb_menu ->{
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, MenuFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.bnb_statistic -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, StatisticFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.bnb_profile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, ProfileFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }

                    else -> false
                }
            }
        }
    }
}