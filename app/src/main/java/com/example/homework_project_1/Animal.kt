package com.example.homework_project_1

interface Animal {
    val weight: Double  // вес в килограммах
    val age: Int        // возраст в годах
    val breed: String   // порода
}

interface Dog : Animal {
    val biteType: BiteType
}

interface Cat : Animal {
    val behaviorType: BehaviorType
}