package setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Dishes: Table("dishes") {
    val name = varchar("name", 100).primaryKey()
    val ingredient = varchar("ingredient", 100).references(Products.name, onDelete = ReferenceOption.CASCADE)
    val weight = integer("weight")
    val calories = integer("calories")
}