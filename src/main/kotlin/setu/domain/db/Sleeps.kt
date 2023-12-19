package setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Sleeps: Table("sleeps") {
    val id = integer("id").autoIncrement().primaryKey()
    val duration = double("duration")
    val date = datetime("date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}