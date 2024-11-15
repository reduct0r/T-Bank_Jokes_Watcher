package com.example.homework_project_1.main.ui.joke_add

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.homework_project_1.R
import com.example.homework_project_1.main.data.JokesRepository
import com.example.homework_project_1.main.data.ViewTyped
import kotlinx.coroutines.launch

class AddJokeActivity : AppCompatActivity() {

    private lateinit var spinnerCategory: Spinner
    private lateinit var questionEditText: EditText
    private lateinit var answerEditText: EditText
    private lateinit var saveButton: Button

    private lateinit var categoriesAdapter: ArrayAdapter<String>
    private var categoriesList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_joke)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        questionEditText = findViewById(R.id.editTextQuestion)
        answerEditText = findViewById(R.id.editTextAnswer)
        saveButton = findViewById(R.id.buttonSave)

        // Получаем список категорий из репозитория
        categoriesList.addAll(JokesRepository.getCategories())
        categoriesList.add(getString(R.string.add_new_category)) // Добавляем опцию "Добавить новую категорию"

        categoriesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = categoriesAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoriesList[position]
                if (selectedCategory == getString(R.string.add_new_category)) {
                    showAddCategoryDialog()
                }
            }
        }

        saveButton.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem.toString()
            val question = questionEditText.text.toString()
            val answer = answerEditText.text.toString()

            if (selectedCategory.isNotBlank() && question.isNotBlank() && answer.isNotBlank()) {
                val joke = ViewTyped.Joke(id = 0, avatar = null, category = selectedCategory, question = question, answer = answer)
                lifecycleScope.launch {
                    JokesRepository.addNewJoke(joke)
                    finish() // Возвращаемся на главный экран после сохранения
                }
            } else {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
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
                spinnerCategory.setSelection(categoriesList.indexOf(newCategory))
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