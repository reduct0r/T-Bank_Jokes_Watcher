package com.example.homework_project_1.main.ui.joke_add

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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.homework_project_1.R
import com.example.homework_project_1.databinding.ActivityAddJokeBinding
import com.example.homework_project_1.main.data.Joke
import com.example.homework_project_1.main.data.JokesRepository
import kotlinx.coroutines.launch
import java.util.UUID

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
            // постоянный доступ к URI
            contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
           binding.imageViewAvatar.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJokeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Получаем список категорий из репозитория
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

        binding.buttonSave.setOnClickListener {
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()
            val question = binding.editTextQuestion.text.toString()
            val answer = binding.editTextAnswer.text.toString()

            if (selectedCategory.isNotBlank() && question.isNotBlank() && answer.isNotBlank()) {
                val joke = Joke(
                    id = UUID.randomUUID().hashCode(),
                    avatar = null,
                    avatarUri = if(selectedImageUri != null) selectedImageUri else null,
                    category = selectedCategory,
                    question = question,
                    answer = answer,
                )
                lifecycleScope.launch {
                    JokesRepository.addNewJoke(joke)
                    finish() // Возвращаемся на главный экран после сохранения
                }
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonSelectAvatar.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
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
                // Добавляем новую категорию в репозиторий
                JokesRepository.addNewCategory(newCategory)
                // Обновляем список категорий в адаптере
                categoriesList.add(categoriesList.size - 1, newCategory)
                categoriesAdapter.notifyDataSetChanged()
                // Устанавливаем выбранной новую категорию
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
}