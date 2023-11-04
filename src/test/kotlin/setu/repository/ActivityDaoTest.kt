package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.Activity
import setu.domain.db.Activities
import setu.domain.repository.ActivityDao
import setu.helpers.activities

class ActivityDaoTest {

    private val activity1 = activities[0]
    private val activity2 = activities[1]
    private val activity3 = activities[2]
    private val activity4 = activities[3]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateActivityTable(): ActivityDao {
        UserDaoTest().populateUserTable()

        SchemaUtils.create(Activities)
        val activityDao = ActivityDao()
        activityDao.save(activity1)
        activityDao.save(activity2)
        activityDao.save(activity3)
        activityDao.save(activity4)
        return activityDao
    }

    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                val activityDao = populateActivityTable()

                Assertions.assertEquals(4, activityDao.getAll().size)
                Assertions.assertEquals(activity1, activityDao.findById(activity1.id))
                Assertions.assertEquals(activity2, activityDao.findById(activity2.id))
                Assertions.assertEquals(activity3, activityDao.findById(activity3.id))
                Assertions.assertEquals(activity4, activityDao.findById(activity4.id))
            }
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get all activities from an empty db`() {
            transaction {
                SchemaUtils.create(Activities)
                val activityDao = ActivityDao()
                Assertions.assertEquals(0, activityDao.getAll().size)
            }
        }

        @Test
        fun `get all activities from a populated table returns all rows`() {
            transaction {
                val activityDao = populateActivityTable()
                Assertions.assertEquals(4, activityDao.getAll().size)
            }
        }

        @Test
        fun `get activities by User Id that doesn't exist, results in no user returned`() {
            transaction {
                val activityDao = populateActivityTable()
                val activities = activityDao.findByUserId(10)
                Assertions.assertEquals(null, if (activities.isEmpty()) null else activities)
            }
        }

        @Test
        fun `get activities by User Id that exists, results in a correct user returned`() {
            transaction {
                val activityDao = populateActivityTable()
                Assertions.assertEquals(arrayListOf(activity1, activity2), activityDao.findByUserId(1))
            }
        }

        @Test
        fun `get all activities over empty table returns none`() {
            transaction {
                SchemaUtils.create(Activities)
                val activityDao = ActivityDao()
                Assertions.assertEquals(0, activityDao.getAll().size)
            }
        }

        @Test
        fun `get activity by Id that doesn't exist, results in no user returned`() {
            transaction {
                val activityDao = populateActivityTable()
                Assertions.assertEquals(activity1, activityDao.findById(1))
            }
        }

        @Test
        fun `get activity by Id that exists, results in correct user returned`() {
            transaction {
                val activityDao = populateActivityTable()
                Assertions.assertEquals(activity3, activityDao.findById(3))
            }
        }
    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting a non-existant activity by Id in table results in no deletion`() {
            transaction {
                val activityDao = populateActivityTable()

                Assertions.assertEquals(4, activityDao.getAll().size)
                activityDao.deleteById(40)
                Assertions.assertEquals(4, activityDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity by Id in table results in record being deleted`() {
            transaction {
                val activityDao = populateActivityTable()

                Assertions.assertEquals(4, activityDao.getAll().size)
                activityDao.deleteById(activity4.id)
                Assertions.assertEquals(3, activityDao.getAll().size)
            }
        }

        @Test
        fun `deleting a non-existant activity by User Id in table results in no deletion`() {
            transaction {
                val activityDao = populateActivityTable()

                Assertions.assertEquals(4, activityDao.getAll().size)
                activityDao.deleteByUserId(40)
                Assertions.assertEquals(4, activityDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity by User Id in table results in record being deleted`() {
            transaction {
                val activityDao = populateActivityTable()

                Assertions.assertEquals(4, activityDao.getAll().size)
                activityDao.deleteByUserId(activity4.userId)
                Assertions.assertEquals(3, activityDao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating existing activity by Id in table results in successful update`() {
            transaction {
                val activityDao = populateActivityTable()

                val newActivity = Activity(
                    description = "Running",
                    duration = 15.5,
                    calories = 120,
                    started = DateTime.now(),
                    userId = 1,
                    id = 3,
                )

                activityDao.update(activity3.id, newActivity)
                Assertions.assertEquals(newActivity, activityDao.findById(3))
            }
        }

        @Test
        fun `updating non-existant activity by Id in table results in no updates`() {
            transaction {
                val activityDao = populateActivityTable()

                val newActivity = Activity(
                    description = "Running",
                    duration = 15.5,
                    calories = 120,
                    started = DateTime.now(),
                    userId = 1,
                    id = 30,
                )

                activityDao.update(30, newActivity)
                Assertions.assertEquals(null, activityDao.findById(30))
                Assertions.assertEquals(4, activityDao.getAll().size)
            }
        }
    }
}