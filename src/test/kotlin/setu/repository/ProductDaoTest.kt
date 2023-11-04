package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.Product
import setu.domain.db.Products
import setu.domain.repository.ProductDao
import setu.helpers.notExistingProductName
import setu.helpers.products

class ProductDaoTest {

    private val product1 = products[0]
    private val product2 = products[1]
    private val product3 = products[2]
    private val product4 = products[3]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateProductTable(): ProductDao {
        SchemaUtils.create(Products)
        val productDao = ProductDao()
        productDao.save(product1)
        productDao.save(product2)
        productDao.save(product3)
        productDao.save(product4)
        return productDao
    }

    @Nested
    inner class CreateProducts {

        @Test
        fun `multiple products added to table can be retrieved successfully`() {
            transaction {
                val productDao = populateProductTable()

                Assertions.assertEquals(4, productDao.getAll().size)
                Assertions.assertEquals(product1, productDao.findByName(product1.name))
                Assertions.assertEquals(product2, productDao.findByName(product2.name))
                Assertions.assertEquals(product3, productDao.findByName(product3.name))
                Assertions.assertEquals(product4, productDao.findByName(product4.name))
            }
        }
    }

    @Nested
    inner class ReadProducts {

        @Test
        fun `get all products from an empty db`() {
            transaction {
                SchemaUtils.create(Products)
                val productDao = ProductDao()
                Assertions.assertEquals(0, productDao.getAll().size)
            }
        }

        @Test
        fun `get all products from a populated table returns all rows`() {
            transaction {
                val productDao = populateProductTable()
                Assertions.assertEquals(4, productDao.getAll().size)
            }
        }

        @Test
        fun `get product by name that doesn't exist, results in no product returned`() {
            transaction {
                val productDao = populateProductTable()
                Assertions.assertEquals(null, productDao.findByName(notExistingProductName))
            }
        }

        @Test
        fun `get product by name that exists, results in a correct product returned`() {
            transaction {
                val productDao = populateProductTable()
                Assertions.assertEquals(product3, productDao.findByName(product3.name))
            }
        }

        @Test
        fun `get all the products over empty table returns none`() {
            transaction {
                SchemaUtils.create(Products)
                val productDao = ProductDao()
                Assertions.assertEquals(0, productDao.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteProducts {

        @Test
        fun `deleting a non-existant product by name in table results in no deletion`() {
            transaction {
                val productDao = populateProductTable()
                Assertions.assertEquals(4, productDao.getAll().size)
                productDao.deleteByName(notExistingProductName)
                Assertions.assertEquals(4, productDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity by name in table results in record being deleted`() {
            transaction {
                val productDao = populateProductTable()
                Assertions.assertEquals(product2, productDao.findByName(product2.name))
                productDao.deleteByName(product2.name)
                Assertions.assertEquals(null, productDao.findByName(product2.name))
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing product by name in table results in successful update`() {
            transaction {
                val activityDao = populateProductTable()

                val newProduct = Product(
                    name = "beef",
                    calories = 150,
                    proteins = 23,
                    fats = 10,
                    carbohydrates = 2,
                )

                activityDao.updateByName(newProduct.name, newProduct)
                Assertions.assertEquals(newProduct, activityDao.findByName(newProduct.name))
            }
        }

        @Test
        fun `updating non-existant activity by Id in table results in no updates`() {
            transaction {
                val activityDao = populateProductTable()

                val newProduct = Product(
                    name = "dorado",
                    calories = 150,
                    proteins = 21,
                    fats = 7,
                    carbohydrates = 5,
                )

                activityDao.updateByName(newProduct.name, newProduct)
                Assertions.assertEquals(null, activityDao.findByName(newProduct.name))
                Assertions.assertEquals(4, activityDao.getAll().size)
            }
        }
    }

}