package setu.helpers

import org.joda.time.DateTime
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
        productName = "test name 2",
        price = 0.5,
        date = DateTime.now()
    ),
    Purchase(
        id = 2,
        userId = 1,
        productName = "test name 1",
        price = 1.5,
        date = DateTime.now()
    ),
    Purchase(
        id = 3,
        userId = 2,
        productName = "test name 1",
        price = 2.2,
        date = DateTime.now()
    ),
)

val purchaseEqualPrice = Purchase(
    id = 4,
    userId = 3,
    productName = "test name 2",
    price = testingPrice,
    date = DateTime.now()
)