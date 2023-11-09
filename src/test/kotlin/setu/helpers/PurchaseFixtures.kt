package setu.helpers

import setu.domain.Purchase

const val existingId = 1
const val nonExistingId = -100
const val nonExistingUserId = -500
const val nonExistingProductName = "asdkkfqwe"
const val testingPrice = 1.0
const val testingPrice1 = 1.1
const val testingPrice2 = 0.75
const val testingPrice3 = 1.27
const val newPrice = 10.25
const val minimumPrice = Double.MIN_VALUE
const val maximumPrice = Double.MAX_VALUE

val purchases = arrayListOf<Purchase>(
    Purchase(
        id = 1,
        userId = 1,
        productName = "cheese",
        price = 0.5,
    ),
    Purchase(
        id = 2,
        userId = 1,
        productName = "beef",
        price = 1.5,
    ),
    Purchase(
        id = 3,
        userId = 2,
        productName = "beef",
        price = 2.2,
    ),
)

val purchaseEqualPrice = Purchase(
    id = 4,
    userId = 3,
    productName = "cheese",
    price = testingPrice,
)