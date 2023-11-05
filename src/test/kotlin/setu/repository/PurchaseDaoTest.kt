package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.db.Purchases
import setu.domain.repository.PurchaseDao
import setu.helpers.nonExistingProductName
import setu.helpers.nonExistingUserId
import setu.helpers.purchases

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
        fun `get purchases by product Name and user Id that don't exist, results in no purchase returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdAndProductName(nonExistingUserId, nonExistingProductName)
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
        fun `get purchases by product Name and user Id that exis, results in no purchase returned`() {
            transaction {
                val purchaseDao = populatePurchaseTable()
                val purchases = purchaseDao.findByUserIdAndProductName(purchase1.userId, purchase1.productName)
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

}