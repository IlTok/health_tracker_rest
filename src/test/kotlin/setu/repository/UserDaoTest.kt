package setu.repository

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import setu.domain.User
import setu.domain.db.Users
import setu.domain.repository.UserDao
import setu.helpers.users
import setu.helpers.nonExistingEmail

class UserDaoTest {

    private val user1 = users[0]
    private val user2 = users[1]
    private val user3 = users[2]

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateUserTable(): UserDao {
        SchemaUtils.create(Users)
        val userDao = UserDao()
        userDao.save(user1)
        userDao.save(user2)
        userDao.save(user3)
        return userDao
    }

    @Nested
    inner class CreateUsers {

        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {
                val userDao = populateUserTable()

                assertEquals(3, userDao.getAll().size)
                assertEquals(user1, userDao.findById(user1.id))
                assertEquals(user2, userDao.findById(user2.id))
                assertEquals(user3, userDao.findById(user3.id))
            }
        }
    }

    @Nested
    inner class ReadUsers {

        @Test
        fun `get all users from a populated table returns all rows`() {
            transaction {
                val userDao = populateUserTable()
                assertEquals(3, userDao.getAll().size)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {
                val userDao = populateUserTable()
                assertEquals(null, userDao.findById(4))
            }
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                val userDao = populateUserTable()
                assertEquals(user2, userDao.findById(2))
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {
                SchemaUtils.create(Users)
                val userDao = UserDao()
                assertEquals(0, userDao.getAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {
                val userDao = populateUserTable()
                assertEquals(null, userDao.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {
                val userDao = populateUserTable()
                assertEquals(user2, userDao.findByEmail(user2.email))
            }
        }
    }

    @Nested
    inner class DeleteUsers {

        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {
                val userDao = populateUserTable()

                assertEquals(3, userDao.getAll().size)
                userDao.delete(4)
                assertEquals(3, userDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {
                val userDao = populateUserTable()

                assertEquals(3, userDao.getAll().size)
                userDao.delete(user3.id)
                assertEquals(2, userDao.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {
                val userDao = populateUserTable()

                val user3Updated = User(3, "new username", "new@email.ie")
                userDao.update(user3.id, user3Updated)
                assertEquals(user3Updated, userDao.findById(3))
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {
                val userDao = populateUserTable()

                val user4Updated = User(4, "new username", "new@email.ie")
                userDao.update(4, user4Updated)
                assertEquals(null, userDao.findById(4))
                assertEquals(3, userDao.getAll().size)
            }
        }
    }
}