package com.example.homework_project_1

class Siamese(
    override val weight: Double,
    override val age: Int
) : Cat {
    override val breed: String = "Siamese"
    override val behaviorType: BehaviorType = BehaviorType.ACTIVE
}