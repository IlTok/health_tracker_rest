package setu.domain

import org.joda.time.DateTime

data class Sleep(
    var id: Int,
    var duration: Double,
    var date: DateTime,
    var userId: Int,
)
