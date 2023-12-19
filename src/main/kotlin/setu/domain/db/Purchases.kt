package setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Purchases: Table("purchases") {
    val id = integer("id").autoIncrement().primaryKey()
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
    val productName = varchar("product_name", 100).references(Products.name, onDelete = ReferenceOption.CASCADE)
    val price = double("price")
    val date = datetime("date")
}