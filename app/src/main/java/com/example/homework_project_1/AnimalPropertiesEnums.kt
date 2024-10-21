package com.example.homework_project_1

// Тип прикуса
enum class BiteType {
    NORMAL,
    OVERBITE,
    UNDERBITE
}

// Тип поведения
enum class BehaviorType {
    ACTIVE,
    PASSIVE
}

// Породы животных
enum class Breed(val displayName: String) {
    HUSKY("Husky"),
    CORGI("Corgi"),
    SIAMESE("Siamese"),
    SCOTTISH_FOLD("Scottish Fold"),
}
