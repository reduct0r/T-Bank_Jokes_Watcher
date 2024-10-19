package com.example.homework_project_1

class Husky(
    override val weight: Double,
    override val age: Int
) : Dog {
    override val breed: String = "Husky"
    override val biteType: BiteType = BiteType.NORMAL
}

class Corgi(
    override val weight: Double,
    override val age: Int
) : Dog {
    override val breed: String = "Corgi"
    override val biteType: BiteType = BiteType.UNDERBITE
}

class ScottishDog(
    override val weight: Double,
    override val age: Int
) : Dog {
    override val breed: String = "Scottish"
    override val biteType: BiteType = BiteType.OVERBITE
}