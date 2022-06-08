package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.guru.englearn2.R

class AAEWActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aaewactivity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}