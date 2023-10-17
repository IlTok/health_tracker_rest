package setu.utils

import setu.domain.User
import setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow
import setu.domain.Activity
import setu.domain.db.Activities

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