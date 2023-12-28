package setu.domain

data class Dish(
    var name: String,
    var ingredient: String,
    val weight: Int,
    val calories: Int,
)