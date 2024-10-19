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
        val breeds = listOf("Husky", "Corgi", "Scottish", "Siamese", "Persian")
        val outputBuilder = StringBuilder()

        for (breed in breeds) {
            val animalType = store.getAnimalType(breed)
            outputBuilder.append("Breed: $breed - Type: $animalType\n")

            val animal = store.createAnimal(breed, weight = 20.0, age = 3)

            if (animal != null) {
                when (animal) {
                    is Dog -> {
                        outputBuilder.append("Breed: ${animal.breed}, Weight: ${animal.weight}kg, Age: ${animal.age},\nBite Type: ${animal.biteType}\n")
                    }
                    is Cat -> {
                        outputBuilder.append("Breed: ${animal.breed}, Weight: ${animal.weight}kg, Age: ${animal.age},\nBehavior Type: ${animal.behaviorType}\n")
                    }
                }
            } else {
                outputBuilder.append("Breed $breed is not available in the store.\n")
            }

            outputBuilder.append("-----\n")
        }
        return outputBuilder.toString()
    }
}

@Composable
fun StringOut(content: String, modifier: Modifier = Modifier) {
    Text(
        text = content,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Homework_project_1Theme {
        StringOut("Sample output")
    }
}