package setu.helpers

import setu.domain.Dish

val unexistingDishName = "nonexisted dish name"
val dishName1 = "dish name 1"
val dishName2 = "dish name 2"
val dishWeight = 120
val dishCalories = 500
val newDishWeight = 555
val newDishCalories = 444

val dishes = listOf<Dish>(
    Dish(
        name = "test dish 1",
        ingredient = "test name 1",
        weight = 150,
        calories = 400
    ),
    Dish(
        name = "test dish 2",
        ingredient = "test name 1",
        weight = 150,
        calories = 400
    ),
    Dish(
        name = "test dish 3",
        ingredient = "test name 2",
        weight = 150,
        calories = 400
    ),
    Dish(
        name = "test dish 4",
        ingredient = "test name 3",
        weight = 150,
        calories = 400
    ),
)