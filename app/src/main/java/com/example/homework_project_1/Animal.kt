package com.example.homework_project_1

// Интерфейс животного
interface Animal {
    val weight: Double  // вес в килограммах
    val age: Int        // возраст в годах
    val breed: Breed   // порода
}

// Интерфейс собаки
interface Dog : Animal {
    val biteType: BiteType
}

// Интерфейс кошки
interface Cat : Animal {
    val behaviorType: BehaviorType
}