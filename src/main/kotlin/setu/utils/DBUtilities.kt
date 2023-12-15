package setu.utils

import org.jetbrains.exposed.sql.ResultRow
import setu.domain.*
import setu.domain.db.*

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToProduct(it: ResultRow) = Product(
    name = it[Products.name],
    calories = it[Products.calories],
    proteins = it[Products.proteins],
    fats = it[Products.fats],
    carbohydrates = it[Products.carbohydrates]
)

fun mapToPurchase(it: ResultRow) = Purchase(
    id = it[Purchases.id],
    userId = it[Purchases.userId],
    productName = it[Purchases.productName],
    price = it[Purchases.price],
)

fun mapToSleep(it: ResultRow) = Sleep(
    id = it[Sleeps.id],
    duration = it[Sleeps.duration],
    date = it[Sleeps.date],
    userId = it[Sleeps.userId],
)