package setu.domain.db

import org.jetbrains.exposed.sql.Table

object Activities : Table("activities") {
    val id = integer("id").autoIncrement().primaryKey()
    val description = varchar("description", 255)
    val duration = integer("duration")
    val calories = integer("calories")
    val started = bool("started")
    val userId = integer("userId")
}