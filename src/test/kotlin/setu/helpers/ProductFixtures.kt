package setu.helpers

import setu.domain.Product

const val notExistingProductName = "chocolate"

val products = arrayListOf<Product>(
    Product(
        name = "beef",
        calories = 150,
        proteins = 23,
        fats = 10,
        carbohydrates = 2,
    ),
    Product(
        name = "cheese",
        calories = 250,
        proteins = 25,
        fats = 60,
        carbohydrates = 5,
    ),
    Product(
        name = "pear",
        calories = 60,
        proteins = 1,
        fats = 1,
        carbohydrates = 35,
    ),
    Product(
        name = "salmon",
        calories = 210,
        proteins = 25,
        fats = 35,
        carbohydrates = 5,
    ),
)