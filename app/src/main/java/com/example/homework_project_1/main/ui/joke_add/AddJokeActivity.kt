package com.example.homework_project_1.main.ui.joke_add

import AddJokeStatus
import AddJokeViewModel
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.ActivityAddJokeBinding
import com.example.homework_project_1.main.data.JokesRepository

class AddJokeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJokeBinding
    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var categoriesList: MutableList<String> = mutableListOf()

    private var selectedImageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            binding.imageViewAvatar.setImageURI(it)
        }
    }

    // Инициализация ViewModel
    private val viewModel: AddJokeViewModel by viewModels {
        AddJokeViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Получение списка категорий (можно также перенести в ViewModel)
        categoriesList.addAll(JokesRepository.getCategories())
        categoriesList.add(getString(R.string.add_new_category)) // Добавляем опцию "Добавить новую категорию"

        categoriesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoriesAdapter

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoriesList[position]
                if (selectedCategory == getString(R.string.add_new_category)) {
                    showAddCategoryDialog()
                }
            }
        }

        // Наблюдение за состоянием добавления шутки
        viewModel.addJokeStatus.observe(this) { status: AddJokeStatus ->
            when (status) {
                is AddJokeStatus.Success -> {
                    Toast.makeText(this, getString(R.string.joke_added_successfully), Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AddJokeStatus.Error -> {
                    Toast.makeText(this, status.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    handleError("Unknown status error")
                }
            }
        }

        binding.buttonSave.setOnClickListener {
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            val question = binding.editTextQuestion.text.toString()
            val answer = binding.editTextAnswer.text.toString()

            if (question.isBlank() || answer.isBlank()) {
                viewModel.addJoke(
                    question = question,
                    answer = answer,
                    category = selectedCategory,
                    avatarUri = selectedImageUri
                )
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSelectAvatar.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        binding.buttonSave.setOnClickListener {
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            val question = binding.editTextQuestion.text.toString()
            val answer = binding.editTextAnswer.text.toString()

            if (question.isNotBlank() && answer.isNotBlank()) {
                viewModel.addJoke(
                    question = question,
                    answer = answer,
                    category = selectedCategory,
                    avatarUri = selectedImageUri
                )
                Toast.makeText(this, getString(R.string.joke_add_in_background), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonBack.setOnClickListener {
            if (binding.editTextQuestion.text.isNotBlank() || binding.editTextAnswer.text.isNotBlank()) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.unsaved_changes))
                    .setMessage(getString(R.string.unsaved_changes_message))
                    .setPositiveButton(getString(R.string.exit)) { _, _ -> finish() }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
            } else {
                finish()
            }
        }
    }

    private fun showAddCategoryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.add_new_category))

        val input = EditText(this)
        input.hint = getString(R.string.enter_category_name)
        builder.setView(input)

        builder.setPositiveButton(getString(R.string.add)) { dialog, _ ->
            val newCategory = input.text.toString().trim()
            if (newCategory.isNotEmpty() && !categoriesList.contains(newCategory)) {
                JokesRepository.addNewCategory(newCategory)
                categoriesList.add(categoriesList.size - 1, newCategory)
                categoriesAdapter.notifyDataSetChanged()
                binding.spinnerCategory.setSelection(categoriesList.indexOf(newCategory))
            } else {
                Toast.makeText(this, getString(R.string.invalid_category), Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }
}