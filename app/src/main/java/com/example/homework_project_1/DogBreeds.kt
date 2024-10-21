package com.example.homework_project_1

class Husky(
    override val weight: Double,
    override val age: Int
) : Dog {
    override val breed: Breed = Breed.HUSKY
    override val biteType: BiteType = BiteType.NORMAL
}

class Corgi(
    override val weight: Double,
    override val age: Int
) : Dog {
    override val breed: Breed = Breed.CORGI
    override val biteType: BiteType = BiteType.UNDERBITE
}