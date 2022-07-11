package ru.guru.englearn2.View.Fragments

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.addTextChangedListener
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.coroutines.*
import ru.guru.englearn2.Model.Lesson
import ru.guru.englearn2.R
import ru.guru.englearn2.ViewModel.EAALVM
import ru.guru.englearn2.databinding.FragmentEaalBinding
import java.io.*
import java.net.URI
import java.net.URLEncoder

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
    private var oldImage: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEaalBinding.inflate(inflater, container, false)

        idLesson = requireActivity().intent.getIntExtra("idLesson", 0)
        idTopic = requireActivity().intent.getIntExtra("idTopic", 0)

        viewModel = ViewModelProvider(this)[EAALVM::class.java]
        lesson = viewModel.getLesson(idLesson)

        // Регистрируем диалог
        dialogLoad = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Загрузка")
            .setCancelable(false)
            .build()

        if (lesson != null) {
            imageFile = File(
                File(requireContext().filesDir.path, "images/lessons"),
                "${lesson!!.title}_${lesson!!.id}.png"
            )
        }

        if (lesson == null) {
            image = resources.getDrawable(R.drawable.rectycle, requireContext().theme)
        } else {
            if (imageFile!!.isFile) {
                image = Drawable.createFromPath(imageFile!!.path)!!
            } else image = resources.getDrawable(R.drawable.rectycle, requireContext().theme)
        }



        binding.apply {

            editTitle.addTextChangedListener {
                if (editTitle.text?.length == 0)
                    titleEditLayout.error = "Поле не должно быть пустое"
                else titleEditLayout.error = null
            }

            imagePicker =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (it.resultCode == -1) {
                            @Suppress("BlockingMethodInNonBlockingContext") val imagePick =
                                Picasso.get()
                                    .load(it.data!!.data!!)
                                    .resize(500, 500)
                                    .rotate(rotatePhoto(it.data!!.data!!))
                                    .centerCrop()
                                    .get()
                            image = BitmapDrawable(resources, imagePick)
                            oldImage = 1
                        }
                        launch(Dispatchers.Main) {
                            lessonImage.setImageDrawable(image)
                        }
                    }
                }


            val permissionPicker =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        imagePicker.launch(intent)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Разрешите использовать мультимедиа",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            var job: Job? = null

            lessonImage.setImageDrawable(image)

            if (lesson != null) {
                editTitle.setText(lesson!!.title)
            }

            lessonImage.setOnClickListener {
                permissionPicker.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            okBtn.setOnClickListener {
                if (editTitle.text?.length != 0) {
                    lesson = viewModel.saveLesson(idLesson, idTopic, editTitle.text!!.toString())
                    if (imageFile == null) {
                        imageFile = File(
                            File(requireContext().filesDir.path, "images/lessons"),
                            "${editTitle.text!!}_${lesson!!.id}.png"
                        )
                    }

                    if (imageFile!!.isFile) {
                        // ДО И ПОСЛЕ RENAMETO ФАЙЛ ЕСЛИ СУДИТЬ ПО ЛОГАМ НЕ ПЕРЕЕМИНОВАН
                        imageFile!!.renameTo(
                            File(
                                "${requireContext().filesDir.path}/images/lessons",
                                "${editTitle.text!!}_${lesson!!.id}.png"
                            )
                        )
                        if (image != resources.getDrawable(
                                R.drawable.rectycle,
                                requireContext().theme
                            )
                        ) {
                            if (oldImage != 0) {
                                job = outputImage(image, imageFile!!)
                                job!!.start()
                            }
                            dialogLoad.show()
                        }
                    } else {
                        if (image != resources.getDrawable(
                                R.drawable.rectycle,
                                requireContext().theme
                            )
                        ) {
                            imageFile!!.createNewFile()
                            job = outputImage(image, imageFile!!)
                            job!!.start()
                            dialogLoad.show()
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        dialogLoad.dismiss()
                        if (job != null){
                            job!!.join()
                        }
                        requireActivity().setResult(-1)
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

    private fun rotatePhoto(uri: Uri): Float {
        var rotate = 0F
        val exifInterface = android.media.ExifInterface(
            requireContext().contentResolver.openInputStream(uri)!!
        )
        when (exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90F
            ExifInterface.ORIENTATION_NORMAL -> rotate = 0F
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180F
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270F
        }
        return rotate
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun outputImage(image: Drawable, imageFile: File) =
        CoroutineScope(Dispatchers.IO).launch(start = CoroutineStart.LAZY) {
            Log.d("My", "Yes")
            val stream = FileOutputStream(imageFile)
            image.toBitmap().compress(Bitmap.CompressFormat.PNG, 0, stream)
            stream.close()
        }

}