package ru.guru.englearn2.View.Activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import ru.guru.englearn2.View.Fragments.WordListFragment
import ru.guru.englearn2.View.Interfaces.SetIdLessonForActivity
import ru.guru.englearn2.databinding.ActivityWordListBinding

class WordListActivity : AppCompatActivity(), SetIdLessonForActivity {

    private lateinit var binding: ActivityWordListBinding
    private var idLesson: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        when {
            idLesson >= 0 -> intent.putExtra("menu_position", 2)
            idLesson == -1 -> intent.putExtra("menu_position", 3)
            idLesson == -2 -> intent.putExtra("menu_position", 3)
        }
        startActivity(intent)
        finish()
    }

    override fun set(idLesson: Int) {
        this.idLesson = idLesson
    }

}