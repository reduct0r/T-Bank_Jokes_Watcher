package com.example.homework_project_1

class ZooStore {

    private val breedToTypeMap = mapOf(
        "husky" to "Dog",
        "corgi" to "Dog",
        "scottish" to "Dog",
        "siamese" to "Cat"
    )

    private val breedToConstructorMap: Map<String, (Double, Int) -> Animal> = mapOf(
        "husky" to ::Husky,
        "corgi" to ::Corgi,
        "scottish" to ::ScottishDog,
        "siamese" to ::Siamese
    )

    fun getAnimalType(breed: String): String {
        return breedToTypeMap[breed.lowercase()] ?: "Unknown"
    }

    fun createAnimal(breed: String, weight: Double, age: Int): Animal? {
        return breedToConstructorMap[breed.lowercase()]?.invoke(weight, age)
    }
}