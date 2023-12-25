package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.Dish
import setu.domain.db.Dishes
import setu.domain.repository.DishDao
import setu.helpers.dishes
import setu.helpers.newDishCalories
import setu.helpers.newDishWeight
import setu.helpers.unexistingDishName

class DishDaoTest {

    private val dish1 = dishes[0]
    private val dish2 = dishes[1]
    private val dish3 = dishes[2]
    private val dish4 = dishes[3]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateDishTable(): DishDao {
        ProductDaoTest().populateProductTable()

        SchemaUtils.create(Dishes)
        val dishDao = DishDao()
        dishDao.save(dish1)
        dishDao.save(dish2)
        dishDao.save(dish3)
        dishDao.save(dish4)
        return dishDao
    }

    @Nested
    inner class CreateSleeps {

        @Test
        fun `multiple dishes added to table can be retrieved successfully`() {
            transaction {
                val dishDao = populateDishTable()

                Assertions.assertEquals(dish1, dishDao.findByName(dish1.name))
                Assertions.assertEquals(dish1, dishDao.findByName(dish1.name))
                Assertions.assertEquals(dish1, dishDao.findByName(dish1.name))
                Assertions.assertEquals(dish1, dishDao.findByName(dish1.name))
            }
        }
    }

    @Nested
    inner class ReadSleeps {

        @Test
        fun `get all dishes from a populated table returns all rows`() {
            transaction {
                val dishDao = populateDishTable()
                Assertions.assertEquals(dishes.size, dishDao.getAll().size)
            }
        }

        @Test
        fun `get dish by name that doesn't exist, results in no sleep returned`() {
            transaction {
                val dishDao = populateDishTable()
                Assertions.assertEquals(null, dishDao.findByName(unexistingDishName))
            }
        }

        @Test
        fun `get dish by id that exists, results in a correct sleep returned`() {
            transaction {
                val dishDao = populateDishTable()
                Assertions.assertEquals(dish2, dishDao.findByName(dish2.name))
            }
        }

        @Test
        fun `get all sleeps over empty table returns none`() {
            transaction {
                SchemaUtils.create(Dishes)
                val dishDao = DishDao()
                Assertions.assertEquals(0, dishDao.getAll().size)
            }
        }

        @Test
        fun `get sleep by name over empty table returns none`() {
            transaction {
                SchemaUtils.create(Dishes)
                val dishDao = DishDao()
                Assertions.assertEquals(null, dishDao.findByName(unexistingDishName))
            }
        }
    }

    @Nested
    inner class DeleteSleeps {

        @Test
        fun `deleting a non-existant dish in table results in no deletion`() {
            transaction {
                val dishDao = populateDishTable()

                Assertions.assertEquals(dishes.size, dishDao.getAll().size)
                dishDao.deleteByName(unexistingDishName)
                Assertions.assertEquals(dishes.size, dishDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleep in table results in record being deleted`() {
            transaction {
                val dishDao = populateDishTable()

                Assertions.assertEquals(dishes.size, dishDao.getAll().size)
                dishDao.deleteByName(dish1.name)
                Assertions.assertEquals(dishes.size - 1, dishDao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateSleeps {

        @Test
        fun `updating existing sleep in table results in successful update`() {
            transaction {
                val dishDao = populateDishTable()
                val newDish = Dish(dish1.name, dish1.ingredient, newDishWeight, newDishCalories)
                dishDao.updateByName(dish1.name, newDish)
                Assertions.assertEquals(newDish, dishDao.findByName(dish1.name))
            }
        }
    }
}