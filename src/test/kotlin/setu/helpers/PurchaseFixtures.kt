package setu.helpers

import setu.domain.Purchase

val nonExistingId = -100
val nonExistingUserId = 500
val nonExistingProductName = "asdkkfqwe"

val purchases = arrayListOf<Purchase>(
    Purchase(
        id = 1,
        userId = 1,
        productName = "cheese",
        price = 2.5,
    ),
    Purchase(
        id = 2,
        userId = 1,
        productName = "beef",
        price = 7.5,
    ),
    Purchase(
        id = 3,
        userId = 2,
        productName = "beef",
        price = 15.2,
    ),
)