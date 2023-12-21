package setu.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import setu.config.DBConfig
import setu.domain.Sleep
import setu.domain.User
import setu.helpers.ServerContainer
import setu.helpers.unexistingSleepId
import setu.helpers.validEmail
import setu.helpers.validName
import setu.utils.jsonNodeToObject
import setu.utils.jsonToObject
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SleepControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    private fun retrieveAllSleeps(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/sleeps").asJson()
    }

    private fun retrieveSleepById(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/sleeps/sleep/${id}").asJson()
    }

    private fun retrieveSleepsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/sleeps/user/${id}").asJson()
    }

    private fun retrieveSleepsByUserIdByYear(userId: Int, year: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/sleeps/user/${userId}/${year}").asJson()
    }

    private fun retrieveSleepsByUserIdByYearMonth(userId: Int, year: Int, month: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/sleeps/user/${userId}/${year}/${month}").asJson()
    }

    private fun deleteSleepById(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/sleeps/sleep/$id").asString()
    }

    private fun updateSleep(
        id: Int, duration: Double, started: DateTime, userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/sleeps/sleep/$id")
            .body(
                """
                {
                  "duration":$duration,
                  "date":"$started",
                  "userId":$userId
                }
            """.trimIndent()
            ).asJson()
    }

    private fun addSleep(
        duration: Double, date: DateTime, userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/sleeps")
            .body(
                """
                {
                   "duration":$duration,
                   "date":"$date",
                   "userId":$userId
                }
            """.trimIndent()
            )
            .asJson()
    }

    private fun setSleeps(userId: Int): ArrayList<HttpResponse<JsonNode>> {
        val firstSleep = addSleep(Random.nextDouble(3.0, 10.0), DateTime.now(), userId)
        val secondSleep = addSleep(Random.nextDouble(3.0, 10.0), DateTime.now(), userId)
        return arrayListOf(firstSleep, secondSleep)
    }

    @Nested
    inner class ReadSleeps {

        @Test
        fun `get all sleeps from the database returns 200 or 404 response`() {
            val response = retrieveAllSleeps()
            if (response.status == 200) {
                val retrievedSleeps = jsonNodeToObject<Array<Sleep>>(response)
                Assertions.assertNotEquals(0, retrievedSleeps.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get sleeps by user, returns 200 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val response = retrieveSleepsByUserId(addedUser.id)
            Assertions.assertEquals(200, response.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user by year, returns 200 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val response = retrieveSleepsByUserIdByYear(addedUser.id, DateTime.now().year)
            Assertions.assertEquals(200, response.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user by year and month, returns 200 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val response =
                retrieveSleepsByUserIdByYearMonth(addedUser.id, DateTime.now().year, DateTime.now().monthOfYear)
            Assertions.assertEquals(200, response.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user by year, returns 404 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val response = retrieveSleepsByUserIdByYear(addedUser.id, 0)
            Assertions.assertEquals(404, response.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user by year and month, returns 404 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val response =
                retrieveSleepsByUserIdByYearMonth(addedUser.id, DateTime.now().year, 0)
            Assertions.assertEquals(404, response.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user id when no sleeps exist returns 404 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val response = retrieveSleepsByUserId(addedUser.id)
            Assertions.assertEquals(404, response.status)

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleeps by user id when no sleeps and user exist returns 404 response`() {
            val userId = Integer.MIN_VALUE
            val response = retrieveSleepsByUserId(userId)
            Assertions.assertEquals(404, response.status)
        }

        @Test
        fun `get sleep by id when sleep exists returns 200 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(200, retrieveSleepById(retrievedSleep.id).status)
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `get sleep by id when sleep doesn't exist returns 404 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            Assertions.assertEquals(404, retrieveSleepById(unexistingSleepId).status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }
    }

    @Nested
    inner class UpdateSleeps {

        @Test
        fun `updating sleep by id, returns 204 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                val updatedSleep = updateSleep(retrievedSleep.id, 15.5, DateTime.now(), retrievedSleep.userId)
                Assertions.assertEquals(204, updatedSleep.status)
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `updating sleep by id when it doesn't exist, returns 204 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            val updatedSleep =
                updateSleep(unexistingSleepId, Random.nextDouble(3.0, 10.0), DateTime.now(), addedUser.id)
            Assertions.assertEquals(404, updatedSleep.status)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }
    }

    @Nested
    inner class DeleteSleeps {

        @Test
        fun `deleting sleep by id, returns a 204 response`() {
            val addedUser: User = jsonToObject(UserControllerTest().addUser(validName, validEmail).body.toString())

            val sleeps = setSleeps(addedUser.id)

            for (i in sleeps) {
                val retrievedSleep: Sleep = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteSleepById(retrievedSleep.id).status)
            }

            Assertions.assertEquals(204, UserControllerTest().deleteUser(addedUser.id).status)
        }

        @Test
        fun `deleting sleep by id when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteSleepById(unexistingSleepId).status)
        }
    }
}