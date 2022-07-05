package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.guru.englearn2.R
import ru.guru.englearn2.View.Fragments.*
import ru.guru.englearn2.View.Interfaces.OnMenuClick
import ru.guru.englearn2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMenuClick {

    companion object {
        var checkBackStackForBNB = 0
    }

    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private val libraryFragment = LibraryFragment()
    private val menuFragment = MenuFragment()
    private val menu2Fragment = Menu2Fragment()
    private val statisticFragment = StatisticFragment()
    private val profileFragment = ProfileFragment()
    private var menuPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuPosition = intent.getIntExtra("menu_position", 0)

        when (menuPosition) {
            0 -> {
                checkBackStackForBNB = 0
            }
            2 -> {
                checkBackStackForBNB = 1
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, libraryFragment)
                    .addToBackStack(null)
                    .commit()
                binding.bnb.menu.getItem(1).isChecked = true
            }
            3 -> {
                checkBackStackForBNB = 2
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, menuFragment)
                    .addToBackStack(null)
                    .commit()
                binding.bnb.menu.getItem(2).isChecked = true
            }
        }

        binding.apply {
            bnb.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.bnb_home -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, mainFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB = 0
                        true
                    }
                    R.id.bnb_library -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, libraryFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB = 1
                        true
                    }
                    R.id.bnb_menu -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, menuFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB = 2
                        true
                    }
                    R.id.bnb_statistic -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, statisticFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB = 3
                        true
                    }
                    R.id.bnb_profile -> {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                            .replace(R.id.fragmentContainerView, profileFragment)
                            .addToBackStack(null)
                            .commit()
                        checkBackStackForBNB = 4
                        true
                    }

                    else -> false
                }
            }
        }
    }

    override fun onBackPressed() {
        if (checkBackStackForBNB == 0) {
            finishAffinity()
        } else {
            checkBackStackForBNB = 0
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, mainFragment)
                .addToBackStack(null)
                .commit()
            binding.bnb.menu.getItem(0).isChecked = true
        }
    }

    override fun onClick(mode: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, menu2Fragment)
            .addToBackStack(null).commit()
        val arg = Bundle()
        arg.putInt("mode", mode)
        menu2Fragment.arguments = arg
    }
}