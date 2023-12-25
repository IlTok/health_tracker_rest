package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.Purchase
import setu.domain.db.Purchases
import setu.domain.repository.PurchaseDao
import setu.helpers.*

class PurchaseDaoTest {

    private val purchase1 = purchases[0]
    private val purchase2 = purchases[1]
    private val purchase3 = purchases[2]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populatePurchaseTable(): PurchaseDao {
        UserDaoTest().populateUserTable()
        ProductDaoTest().populateProductTable()

        SchemaUtils.create(Purchases)

        val purchaseDao = PurchaseDao()
        purchaseDao.save(purchase1)
        purchaseDao.save(purchase2)
        purchaseDao.save(purchase3)
        return purchaseDao
    }

    @Nested
    inner class CreatePurchases {

        @Test
        fun `multiple purchases added to table can be retrieved successfully`() {
            transaction {
                val purchaseDao = populatePurchaseTable()

                Assertions.assertEquals(3, purchaseDao.getAll().size)
            }
        }
    }

    @Nested
    inner class ReadPurchases {

        @Test
        fun `get all purchases from an empty db`() {
            transaction {
                SchemaUtils.create(Purchases)
                val purchaseDao = PurchaseDao()
                Assertions.assertEquals(0, purchaseDao.getAll().size)
            }
        }

        @Test
        fun `get all purchases from a populated table returns all rows`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                Assertions.assertEquals(3, purchaseDao.getAll().size)
            }
        }

        @Test
        fun `get purchases by user Id that doesn't exist, results in no purchase returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserId(nonExistingUserId)
                Assertions.assertEquals(null, if (purchases.isEmpty()) null else purchases)
            }
        }

        @Test
        fun `get purchases by product Name that doesn't exist, results in no purchase returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByProductName(nonExistingProductName)
                Assertions.assertEquals(null, if (purchases.isEmpty()) null else purchases)
            }
        }

        @Test
        fun `get purchases by User Id that exist, results in a correct user returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserId(purchase1.userId)
                Assertions.assertEquals(2, purchases.size)
            }
        }

        @Test
        fun `get purchases by product Name that exist, results in no purchase returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByProductName(purchase1.productName)
                Assertions.assertEquals(1, purchases.size)
            }
        }

        @Test
        fun `get all purchases over empty table returns none`() {
            transaction {
                SchemaUtils.create(Purchases)
                val purchaseDao = PurchaseDao()
                Assertions.assertEquals(0, purchaseDao.getAll().size)
            }
        }

        @Test
        fun `get purchase by Id that doesn't exist`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                Assertions.assertEquals(null, purchaseDao.findById(nonExistingId))
            }
        }

        @Test
        fun `get purchase by Id that exists`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                Assertions.assertEquals(purchase1, purchaseDao.findById(purchase1.id))
            }
        }

        @Test
        fun `get purchases where price are greater than`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findPurchasesWherePriceGreaterThan(testingPrice)
                Assertions.assertEquals(2, purchases.size)
                for (i in purchases) {
                    Assertions.assertTrue(i.price > testingPrice)
                }
            }
        }

        @Test
        fun `get purchases where price are greater than, with equal price of purchase in db`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                purchaseDao.save(purchaseEqualPrice)
                val purchases = purchaseDao.findPurchasesWherePriceGreaterThan(testingPrice)
                Assertions.assertEquals(2, purchases.size)
                for (i in purchases) {
                    Assertions.assertTrue(i.price > testingPrice)
                }
            }
        }

        @Test
        fun `get purchases where price are greater than, over empty table returns none`() {
            transaction {
                SchemaUtils.create(Purchases)
                val purchaseDao = PurchaseDao()
                Assertions.assertEquals(0, purchaseDao.findPurchasesWherePriceGreaterThan(testingPrice).size)
            }
        }

        @Test
        fun `get purchases where price are less than`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findPurchasesWherePriceLessThan(testingPrice)
                Assertions.assertEquals(1, purchases.size)
                for (i in purchases) {
                    Assertions.assertTrue(i.price < testingPrice)
                }
            }
        }

        @Test
        fun `get purchases where price are less than, with equal price of purchase in db`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                purchaseDao.save(purchaseEqualPrice)
                val purchases = purchaseDao.findPurchasesWherePriceLessThan(testingPrice)
                Assertions.assertEquals(1, purchases.size)
                for (i in purchases) {
                    Assertions.assertTrue(i.price < testingPrice)
                }
            }
        }

        @Test
        fun `get purchases where price are less than, over empty table returns none`() {
            transaction {
                SchemaUtils.create(Purchases)
                val purchaseDao = PurchaseDao()
                Assertions.assertEquals(0, purchaseDao.findPurchasesWherePriceLessThan(testingPrice).size)
            }
        }

        @Test
        fun `get purchases by User Id and by Year that exist`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdByYear(purchase1.userId, DateTime.now().year)
                Assertions.assertEquals(2, purchases.size)
            }
        }

        @Test
        fun `get purchases by User Id and by Year that don't exist`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdByYear(purchase1.userId, 0)
                Assertions.assertEquals(0, purchases.size)
            }
        }

        @Test
        fun `get purchases by User Id and by Year and Month that exist`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdByYearMonth(purchase1.userId, DateTime.now().year, DateTime.now().monthOfYear)
                Assertions.assertEquals(2, purchases.size)
            }
        }

        @Test
        fun `get purchases by User Id and by Year and Month that don't exist`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdByYearMonth(purchase1.userId, DateTime.now().year, 0)
                Assertions.assertEquals(0, purchases.size)
            }
        }
    }

    @Nested
    inner class DeletePurchases {

        @Test
        fun `deleting a non-existant purchase by Id in table results in no deletion`() {
            transaction {
                val purchaseDao = populatePurchaseTable()

                Assertions.assertEquals(3, purchaseDao.getAll().size)
                purchaseDao.deleteById(nonExistingUserId)
                Assertions.assertEquals(3, purchaseDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing purchase by Id in table results in record being deleted`() {
            transaction {
                val purchaseDao = populatePurchaseTable()

                Assertions.assertEquals(3, purchaseDao.getAll().size)
                purchaseDao.deleteById(purchase1.id)
                Assertions.assertEquals(2, purchaseDao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdatePurchases {

        @Test
        fun `updating the existing purchase by Id in table results in successful update`() {
            transaction {
                val purchaseDao = populatePurchaseTable()

                val newPurchase = Purchase(
                    id = existingId,
                    userId = 2,
                    productName = products[3].name,
                    price = 3.2,
                    date = DateTime.now()
                )

                purchaseDao.updateById(existingId, newPurchase)
                Assertions.assertEquals(newPurchase, purchaseDao.findById(existingId))
            }
        }

        @Test
        fun `updating non-existant purchase by Id in table results in no updates`() {
            transaction {
                val purchaseDao = populatePurchaseTable()

                val newPurchase = Purchase(
                    id = nonExistingId,
                    userId = 2,
                    productName = products[3].name,
                    price = 3.2,
                    date = DateTime.now()
                )

                purchaseDao.updateById(nonExistingId, newPurchase)
                Assertions.assertEquals(null, purchaseDao.findById(nonExistingId))
            }
        }
    }

}