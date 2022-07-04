package ru.guru.englearn2.View.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.*
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.ViewModel.EAALVM
import ru.guru.englearn2.databinding.FragmentEaalBinding
import java.io.File
import java.io.FileOutputStream

class EAALFragment : Fragment(R.layout.fragment_eaal) {

    private lateinit var binding: FragmentEaalBinding
    private var idTopic: Int = 0
    private lateinit var lesson: Lesson
    private lateinit var viewModel: EAALVM
    private var idLesson: Int = 0
    private var imagePicker: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEaalBinding.inflate(inflater, container, false)
        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        viewModel = ViewModelProvider(this)[EAALVM::class.java]
        lesson = viewModel.getLesson(idLesson)
        val lessonTemp = "${lesson.title}.png"
        val oldImage = Drawable.createFromPath(
            File(File(requireContext().filesDir.path, "images"), "${lesson.title}.png").path)
        var newImageTemp: Bitmap? = null

        binding.apply {

            imagePicker =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == -1) {
                        CoroutineScope(Dispatchers.IO).launch {
                            newImageTemp =
                                Picasso.get()
                                .load(it.data?.data!!)
                                .resize(500, 500)
                                .centerCrop()
                                .get()
                            requireActivity().runOnUiThread {
                                lessonImage.setImageBitmap(newImageTemp)
                            }
                        }
                    }
                }

            editTitle.addTextChangedListener {
                if (editTitle.text?.length == 0) titleEditLayout.error =
                    "Поле не должно быть пустым"
                else titleEditLayout.error = null
            }

            okBtn.setOnClickListener {
                var newImageFile: File? = null
                val dialog: AlertDialog = SpotsDialog.Builder().setContext(requireContext()).setMessage("Загрузка").setCancelable(false).build()
                val jobr = CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY){
                    val stream = FileOutputStream(newImageFile)
                    newImageTemp!!.compress(Bitmap.CompressFormat.PNG, 0, stream)
                    Log.d("My", "1")
                    stream.close()

                }
                    if (editTitle.text?.length != 0) {
                        viewModel.saveLesson(idLesson, editTitle.text!!.toString())
                        val dirImages = File(requireContext().filesDir.path, "images")
                        val tempImage = File(dirImages, lessonTemp)
                        if (newImageTemp == null) {
                            tempImage.renameTo(File(dirImages, "${lesson.title}.png"))
                        } else {
                            newImageFile = File(dirImages, "${lesson.title}.png")
                            if (tempImage.isFile) {
                                tempImage.delete()
                                newImageFile.createNewFile()
                                jobr.start()
                                dialog.show()
                            } else {
                                tempImage.createNewFile()
                            }
                            requireActivity().setResult(-1)
                        }
                    } else Toast.makeText(requireContext(), "Поле заголовка пустое", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.IO).launch {
                    jobr.join()
                    Log.d("My", "2")
                    dialog.dismiss()
                    requireActivity().finish()
                }

            }

            lessonImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                imagePicker!!.launch(intent)

            }

            /** Режим редактирования */

            if (idLesson != -1) {
                editTitle.setText(lesson.title)
                lessonImage.setImageDrawable(oldImage)
            }
        }

        return binding.root
    }

}