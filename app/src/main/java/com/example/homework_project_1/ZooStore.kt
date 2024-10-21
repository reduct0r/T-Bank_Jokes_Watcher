package com.example.homework_project_1

class ZooStore {

    fun getAnimalType(animal: Animal): String {
        return when (animal) {
            is Dog -> "Dog"
            is Cat -> "Cat"
            else -> "Unknown"
        }
    }

    fun createAnimal(animal: Animal): Animal {
        return animal
    }
}