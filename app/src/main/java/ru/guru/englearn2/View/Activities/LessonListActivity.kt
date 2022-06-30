package ru.guru.englearn2.View.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.guru.englearn2.R
import ru.guru.englearn2.databinding.ActivityLessonListBinding

class LessonListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}