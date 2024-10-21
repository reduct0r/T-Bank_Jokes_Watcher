package com.example.homework_project_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.homework_project_1.ui.theme.Homework_project_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainOutput = mainOutput()

        setContent {
            Homework_project_1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StringOut(
                        content = mainOutput,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun mainOutput(): String {
        val store = ZooStore()
        val animals = listOf(
            Husky(weight = 20.0, age = 3),
            Corgi(weight = 20.0, age = 3),
            ScottishFold(weight = 4.0, age = 2),
            Siamese(weight = 1.0, age = 1)
        )
        val outputBuilder = StringBuilder()

        for (animal in animals) {
            val animalType = store.getAnimalType(animal)
            outputBuilder.append("Type: $animalType\n")

            when (animal) {
                is Dog -> {
                    outputBuilder.append(
                        "Breed: ${animal.breed.displayName}, Weight: ${animal.weight} kg, Age: ${animal.age},\n" +
                                "Bite Type: ${animal.biteType}\n"
                    )
                }
                is Cat -> {
                    outputBuilder.append(
                        "Breed: ${animal.breed.displayName}, Weight: ${animal.weight} kg, Age: ${animal.age},\n" +
                                "Behavior Type: ${animal.behaviorType}\n"
                    )
                }
                else -> {
                    outputBuilder.append("Unknown animal\n")
                }
            }

            outputBuilder.append("-----\n")
        }
        return outputBuilder.toString()
    }

    @Composable
    fun StringOut(content: String, modifier: Modifier = Modifier) {
        Text(
            text = content,
            modifier = modifier
        )
    }
}

