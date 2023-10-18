package setu.helpers

import org.joda.time.DateTime
import setu.domain.Activity

val activities = arrayListOf<Activity>(
    Activity(
        description = "Running",
        duration = 15.5,
        calories = 120,
        started = DateTime.now(),
        userId = 1,
        id = 1,
    ),
    Activity(
        description = "Jumping",
        duration = 28.2,
        calories = 150,
        started = DateTime.now(),
        userId = 1,
        id = 2,
    ),
    Activity(
        description = "Bike",
        duration = 75.1,
        calories = 70,
        started = DateTime.now(),
        userId = 2,
        id = 3,
    ),
    Activity(
        description = "Gym",
        duration = 55.7,
        calories = 190,
        started = DateTime.now(),
        userId = 3,
        id = 4,
    ),
)