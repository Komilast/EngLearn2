package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.guru.englearn2.databinding.ActivityWordListBinding

class WordListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWordListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}