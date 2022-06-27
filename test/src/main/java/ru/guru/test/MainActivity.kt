package ru.guru.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.*
import ru.guru.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var onActivityResult: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var int = 0
        val job = CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY){
            while (int != 1000000000){
                int++
            }
        }
        binding.apply {
            button.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                job.start()
                val dialog = SpotsDialog.Builder().setContext(this@MainActivity).setCancelable(false).setMessage("Загрузка").build()
                dialog.show()
                CoroutineScope(Dispatchers.IO).launch {
                    job.join()
                    dialog.dismiss()
                    runOnUiThread { Toast.makeText(this@MainActivity, "Готово $int", Toast.LENGTH_SHORT).show() }
                     }

        }

        }

    }





}