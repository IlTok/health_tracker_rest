package setu.helpers

import org.joda.time.DateTime
import setu.domain.Activity

const val updatedDescription = "Updated Description"
const val updatedDuration = 30.0
const val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val activities = arrayListOf<Activity>(
    Activity(
        description = "Running Test",
        duration = 15.5,
        calories = 120,
        started = DateTime.now(),
        userId = 1,
        id = 1,
    ),
    Activity(
        description = "Jumping Test",
        duration = 28.2,
        calories = 150,
        started = DateTime.now(),
        userId = 1,
        id = 2,
    ),
    Activity(
        description = "Bike Test",
        duration = 75.1,
        calories = 70,
        started = DateTime.now(),
        userId = 2,
        id = 3,
    ),
    Activity(
        description = "Gym Test",
        duration = 55.7,
        calories = 190,
        started = DateTime.now(),
        userId = 3,
        id = 4,
    ),
)