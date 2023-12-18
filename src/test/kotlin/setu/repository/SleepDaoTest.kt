package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.db.Sleeps
import setu.domain.repository.SleepDao
import setu.helpers.sleeps
import setu.helpers.unexistingSleepId
import setu.helpers.updatedSleep

class SleepDaoTest {

    private val sleep1 = sleeps[0]
    private val sleep2 = sleeps[1]
    private val sleep3 = sleeps[2]
    private val sleep4 = sleeps[3]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateSleepTable(): SleepDao {
        UserDaoTest().populateUserTable()

        SchemaUtils.create(Sleeps)
        val sleepDao = SleepDao()
        sleepDao.save(sleep1)
        sleepDao.save(sleep2)
        sleepDao.save(sleep3)
        sleepDao.save(sleep4)
        return sleepDao
    }

    @Nested
    inner class CreateSleeps {

        @Test
        fun `multiple sleeps added to table can be retrieved successfully`() {
            transaction {
                val sleepDao = populateSleepTable()

                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
                Assertions.assertEquals(sleep1, sleepDao.findById(sleep1.id))
                Assertions.assertEquals(sleep2, sleepDao.findById(sleep2.id))
                Assertions.assertEquals(sleep3, sleepDao.findById(sleep3.id))
                Assertions.assertEquals(sleep4, sleepDao.findById(sleep4.id))
            }
        }
    }

    @Nested
    inner class ReadSleeps {

        @Test
        fun `get all sleeps from a populated table returns all rows`() {
            transaction {
                val sleepDao = populateSleepTable()
                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
            }
        }

        @Test
        fun `get sleep by id that doesn't exist, results in no sleep returned`() {
            transaction {
                val sleepDao = populateSleepTable()
                Assertions.assertEquals(null, sleepDao.findById(unexistingSleepId))
            }
        }

        @Test
        fun `get sleep by id that exists, results in a correct sleep returned`() {
            transaction {
                val sleepDao = populateSleepTable()
                Assertions.assertEquals(sleep3, sleepDao.findById(sleep3.id))
            }
        }

        @Test
        fun `get all sleeps over empty table returns none`() {
            transaction {
                SchemaUtils.create(Sleeps)
                val sleepDao = SleepDao()
                Assertions.assertEquals(0, sleepDao.getAll().size)
            }
        }

        @Test
        fun `get sleeps by userId that doesn't exist, results in no sleeps returned`() {
            transaction {
                val sleepDao = populateSleepTable()
                Assertions.assertEquals(0, sleepDao.findByUserId(unexistingSleepId).size)
            }
        }

        @Test
        fun `get sleeps by userId that exists, results in correct sleep returned`() {
            transaction {
                val sleepDao = populateSleepTable()
                var count = 0
                for (i in sleeps){
                    if (i.userId == sleep1.userId)
                        count++
                }
                Assertions.assertEquals(count, sleepDao.findByUserId(sleep1.userId).size)
            }
        }
    }

    @Nested
    inner class DeleteSleeps {

        @Test
        fun `deleting a non-existant sleep in table results in no deletion`() {
            transaction {
                val sleepDao = populateSleepTable()

                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
                sleepDao.delete(unexistingSleepId)
                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleep in table results in record being deleted`() {
            transaction {
                val sleepDao = populateSleepTable()

                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
                sleepDao.delete(sleep2.id)
                Assertions.assertEquals(sleeps.size - 1, sleepDao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateSleeps {

        @Test
        fun `updating existing sleep in table results in successful update`() {
            transaction {
                val sleepDao = populateSleepTable()

                sleepDao.update(updatedSleep.id, updatedSleep)
                Assertions.assertEquals(updatedSleep, sleepDao.findById(updatedSleep.id))
            }
        }

        @Test
        fun `updating non-existant sleep in table results in no updates`() {
            transaction {
                val sleepDao = populateSleepTable()

                sleepDao.update(unexistingSleepId, updatedSleep)
                Assertions.assertEquals(null, sleepDao.findById(unexistingSleepId))
                Assertions.assertEquals(sleeps.size, sleepDao.getAll().size)
            }
        }
    }
}