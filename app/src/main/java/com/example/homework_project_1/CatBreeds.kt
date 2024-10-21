package com.example.homework_project_1

class Siamese(
    override val weight: Double,
    override val age: Int
) : Cat {
    override val breed: Breed = Breed.SIAMESE
    override val behaviorType: BehaviorType = BehaviorType.ACTIVE
}

class ScottishFold(
    override val weight: Double,
    override val age: Int
) : Cat {
    override val breed: Breed = Breed.SCOTTISH_FOLD
    override val behaviorType: BehaviorType = BehaviorType.PASSIVE
}