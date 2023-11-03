package setu.domain.db

import org.jetbrains.exposed.sql.Table

object Products : Table("products") {
    val name = varchar("name", 100).primaryKey()
    val calories = integer("calories")
    val proteins = integer("proteins")
    val fats = integer("fats")
    val carbohydrates = integer("carbohydrates")
}