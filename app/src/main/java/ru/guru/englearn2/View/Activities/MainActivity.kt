package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Fragments.*
import ru.guru.englearn2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        val checkBackStackForBNB = ArrayList<Int>()
    }

    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private val libraryFragment = LibraryFragment()
    private val menuFragment = MenuFragment()
    private val statisticFragment = StatisticFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkBackStackForBNB.add(0)

        binding.apply {
            bnb.setOnItemSelectedListener {

                when(it.itemId){
                    R.id.bnb_home -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, mainFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB.add(0)
                        true
                    }
                    R.id.bnb_library -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, libraryFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB.add(1)
                        true
                    }
                    R.id.bnb_menu ->{
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, menuFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB.add(2)
                        true
                    }
                    R.id.bnb_statistic -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, statisticFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB.add(3)
                        true
                    }
                    R.id.bnb_profile -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, profileFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB.add(4)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    override fun onBackPressed() {
        if (checkBackStackForBNB.size == 1){
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
            binding.bnb.menu.getItem(checkBackStackForBNB.get(checkBackStackForBNB.count() - 2)).isChecked = true
            checkBackStackForBNB.removeAt(checkBackStackForBNB.count() - 1)
        }
    }
}