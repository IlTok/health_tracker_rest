package setu.helpers

import org.joda.time.DateTime
import setu.domain.Sleep

val sleepDate = DateTime.parse("2020-06-11T05:59:27.258Z")
val unexistingSleepId = Integer.MIN_VALUE

val sleeps = arrayListOf<Sleep>(
    Sleep(id = 1, duration = 9.5, date = DateTime.now(), userId = 1),
    Sleep(id = 2, duration = 8.5, date = DateTime.now(), userId = 1),
    Sleep(id = 3, duration = 7.5, date = DateTime.now(), userId = 2),
    Sleep(id = 4, duration = 6.5, date = DateTime.now(), userId = 3),
)

val updatedSleep = Sleep(id = 1, duration = 12.2, date = DateTime.now(), userId = 1)