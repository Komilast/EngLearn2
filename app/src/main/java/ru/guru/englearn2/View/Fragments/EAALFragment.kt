package ru.guru.englearn2.View.Fragments

import android.app.AlertDialog
import android.app.Instrumentation
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
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
    private lateinit var viewModel: EAALVM
    private var lesson: Lesson? = null
    private var idLesson = 0
    private var idTopic = 0
    private lateinit var imagePicker: ActivityResultLauncher<Intent>
    private lateinit var image: Drawable
    private var imageFile: File? = null
    private lateinit var dialogLoad: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEaalBinding.inflate(inflater, container, false)

        // Получаем данные из другой активности
        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        idTopic = requireActivity().intent.getIntExtra("idTopic", 0)

        // Получаем урок (в случае если нужно его создать, то получим null)
        viewModel = ViewModelProvider(this)[EAALVM::class.java]
        lesson = viewModel.getLesson(idLesson)

        // Регистрируем диалог
        dialogLoad = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Загрузка")
            .setCancelable(false)
            .build()

        // Получаем файл картинки урока в случае если урок редактируется
        if (lesson != null) imageFile = File(
            File(requireContext().filesDir.path, "images"),
            "${lesson?.title}.png"
        )

        // Задаём текущую картинку урока
        image = if (lesson == null) {
            resources.getDrawable(R.drawable.rectycle, requireContext().theme)
        } else {
            if (imageFile!!.isFile)
                Drawable.createFromPath(imageFile!!.path)!!
            else resources.getDrawable(R.drawable.rectycle, requireContext().theme)
        }

        // Региструруем зарание корутину для записи картинки в файл
        @Suppress("BlockingMethodInNonBlockingContext") val job =
            CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY) {
                val stream = FileOutputStream(imageFile)
                image.toBitmap().compress(Bitmap.CompressFormat.PNG, 0, stream)
                stream.close()
            }

        // Регистрируем получение картинки из телефона
        imagePicker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == -1) {
                val imagePick = Picasso.get()
                    .load(it.data!!.data!!)
                    .resize(500, 500)
                    .centerCrop()
                    .get()
                image = BitmapDrawable(resources, imagePick)
            }
        }

        binding.apply {

            // Проверка правильности ввода
            editTitle.addTextChangedListener {
                if (editTitle.text?.length == 0)
                    titleEditLayout.error = "Поле не должно быть пустое"
                else titleEditLayout.error = null
            }

            // Установка картинки в ImageView
            lessonImage.setImageDrawable(image)

            // Запись текста в поле для ввода если урок редактируется
            if (lesson != null) {
                editTitle.setText(lesson!!.title)
            }

            // Действие при нажатии на картинку и переход в каталог телефона для поиска картинок
            lessonImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                imagePicker.launch(intent)
            }

            okBtn.setOnClickListener {
                if (editTitle.text?.length != 0) {
                    viewModel.saveLesson(idLesson, idTopic, editTitle.text!!.toString())

                    if (imageFile == null) {
                        imageFile = File(
                            File(requireContext().filesDir.path, "images"),
                            "${editTitle.text!!}.png"
                        )
                    }

                    if (imageFile!!.isFile) {
                        imageFile!!.renameTo(
                            File(
                                File(requireContext().filesDir.path, "images"),
                                "${editTitle.text!!}.png"
                            )
                        )
                        if (image != resources.getDrawable(
                                R.drawable.rectycle,
                                requireContext().theme
                            )
                        ) {
                            job.start()
                            dialogLoad.show()
                        }
                    } else {
                        if (image != resources.getDrawable(
                                R.drawable.rectycle,
                                requireContext().theme
                            )
                        ) {
                            imageFile!!.createNewFile()
                            job.start()
                            dialogLoad.show()
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        job.join()
                        dialogLoad.dismiss()
                        requireActivity().finish()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Поле не должно быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }
}