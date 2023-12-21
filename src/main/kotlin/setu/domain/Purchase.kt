package setu.domain

import org.joda.time.DateTime

data class Purchase(
    var id: Int,
    var userId: Int,
    var productName: String,
    var price: Double,
    var date: DateTime,
) : MainDataClass