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
import setu.domain.repository.UserDAO
import setu.helpers.users
import setu.helpers.nonExistingEmail

class UserDaoTest {

    val user1 = users[0]
    val user2 = users[1]
    val user3 = users[2]

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        return userDAO
    }

    @Nested
    inner class CreateUsers {

        @Test
        fun `multiple users added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                assertEquals(user1, userDAO.findById(user1.id))
                assertEquals(user2, userDAO.findById(user2.id))
                assertEquals(user3, userDAO.findById(user3.id))
            }
        }
    }

    @Nested
    inner class ReadUsers {

        @Test
        fun `get all users from a populated table returns all rows`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by id that doesn't exist, results in no user returned`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(null, userDAO.findById(4))
            }
        }

        @Test
        fun `get user by id that exists, results in a correct user returned`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(null, userDAO.findById(4))
            }
        }

        @Test
        fun `get all users over empty table returns none`() {
            transaction {

                SchemaUtils.create(Users)
                val userDAO = UserDAO()

                assertEquals(0, userDAO.getAll().size)
            }
        }

        @Test
        fun `get user by email that doesn't exist, results in no user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(null, userDAO.findByEmail(nonExistingEmail))
            }
        }

        @Test
        fun `get user by email that exists, results in correct user returned`() {
            transaction {

                val userDAO = populateUserTable()

                assertEquals(user2, userDAO.findByEmail(user2.email))
            }
        }
    }

    @Nested
    inner class DeleteUsers {

        @Test
        fun `deleting a non-existant user in table results in no deletion`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(4)
                assertEquals(3, userDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing user in table results in record being deleted`() {
            transaction {
                val userDAO = populateUserTable()

                assertEquals(3, userDAO.getAll().size)
                userDAO.delete(user3.id)
                assertEquals(2, userDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class UpdateUsers {

        @Test
        fun `updating existing user in table results in successful update`() {
            transaction {

                val userDAO = populateUserTable()

                val user3Updated = User(3, "new username", "new@email.ie")
                userDAO.update(user3.id, user3Updated)
                assertEquals(user3Updated, userDAO.findById(3))
            }
        }

        @Test
        fun `updating non-existant user in table results in no updates`() {
            transaction {

                val userDAO = populateUserTable()

                val user4Updated = User(4, "new username", "new@email.ie")
                userDAO.update(4, user4Updated)
                assertEquals(null, userDAO.findById(4))
                assertEquals(3, userDAO.getAll().size)
            }
        }
    }
}