package setu.domain

data class Purchase(
    var id: Int,
    var userId: Int,
    var productName: String,
    var price: Double,
)