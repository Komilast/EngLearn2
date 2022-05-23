package ru.guru.exceltorealm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val realmHelper = RealmHelper(this@MainActivity)
            val job = CoroutineScope(Dispatchers.IO).launch {
                realmHelper.createBase()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Готово", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}