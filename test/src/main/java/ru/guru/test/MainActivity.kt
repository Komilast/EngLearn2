package ru.guru.test

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.tabs.TabLayoutMediator
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmObject
import org.xml.sax.Parser
import ru.guru.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            navBar.selectedItemId = R.id.main

            navBar.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.main -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, MainFragment())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.page_1 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, Fragment1())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.page_2 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, Fragment2())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    R.id.page_3 -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView, Fragment3())
                            .addToBackStack(null)
                            .commit()
                        true
                    }
                    else -> {false}
                }
            }
        }

    }



}