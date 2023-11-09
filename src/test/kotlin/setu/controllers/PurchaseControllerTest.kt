package setu.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import setu.config.DBConfig
import setu.domain.Product
import setu.domain.Purchase
import setu.domain.User
import setu.helpers.*
import setu.utils.jsonNodeToObject
import setu.utils.jsonToObject
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PurchaseControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    private val userControllerTest = UserControllerTest()
    private val productControllerTest = ProductControllerTest()

    private fun retrieveAllPurchases(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/purchases").asJson()
    }

    private fun retrievePurchasesByUserId(userId: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/purchases/user/${userId}").asJson()
    }

    private fun retrievePurchaseById(id: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/purchases/${id}").asJson()
    }

    private fun retrievePurchasesByProductName(productName: String): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/purchases/product/${productName}").asJson()
    }

    private fun deletePurchaseById(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/purchases/$id").asString()
    }

    private fun updatePurchase(
        id: Int,
        userId: Int,
        productName: String,
        price: Double,
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/purchases/${id}")
            .body(
                """
                {
                  "userId":"$userId",
                  "productName":"$productName",
                  "price":"$price"
                }
            """.trimIndent()
            ).asJson()
    }

    private fun addPurchase(
        userId: Int,
        productName: String,
        price: Double,
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/purchases")
            .body(
                """
                {
                  "userId":"$userId",
                  "productName":"$productName",
                  "price":"$price"
                }
            """.trimIndent()
            ).asJson()
    }

    private fun addThreePurchases(userId: Int, productName: String): List<HttpResponse<JsonNode>> {
        val purchaseResponse1 = addPurchase(userId, productName, testingPrice1)
        assertEquals(201, purchaseResponse1.status)
        val purchaseResponse2 = addPurchase(userId, productName, testingPrice2)
        assertEquals(201, purchaseResponse2.status)
        val purchaseResponse3 = addPurchase(userId, productName, testingPrice3)
        assertEquals(201, purchaseResponse3.status)
        return listOf(purchaseResponse1, purchaseResponse2, purchaseResponse3)
    }

    @Nested
    inner class WritePurchases {

        @Test
        fun `write purchase, returns 404 response`() {
            val purchaseResponse1 = addPurchase(nonExistingId, nonExistingProductName, testingPrice1)
            assertEquals(404, purchaseResponse1.status)
        }
    }

    @Nested
    inner class ReadPurchases {

        @Test
        fun `get all purchases from the database returns 200 or 404 response`() {
            val response = retrieveAllPurchases()
            if (response.status == 200) {
                val retrievedPurchases = jsonNodeToObject<Array<Purchase>>(response)
                Assertions.assertNotEquals(0, retrievedPurchases.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get purchase by Id from the database returns 200 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val purchaseResponse = addPurchase(retrievedUser.id, retrievedProduct.name, testingPrice)
            assertEquals(201, purchaseResponse.status)

            val retrievedPurchase = jsonToObject<Purchase>(purchaseResponse.body.toString())
            val response = retrievePurchaseById(retrievedPurchase.id)
            assertEquals(200, response.status)
            assertEquals(retrievedProduct.name, retrievedPurchase.productName)

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            deletePurchaseById(retrievedPurchase.id)
        }

        @Test
        fun `get purchase by Id that doesn't exist, returns 404 response`() {
            assertEquals(404, retrievePurchaseById(nonExistingId).status)
        }

        @Test
        fun `get purchases by User Id from the database returns 200 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val addedResponses = addThreePurchases(retrievedUser.id, retrievedProduct.name)

            val response = retrievePurchasesByUserId(retrievedUser.id)
            assertEquals(200, response.status)
            val retrievedPurchase = jsonNodeToObject<Array<Purchase>>(response)
            assertEquals(3, retrievedPurchase.size)

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            for (i in addedResponses) {
                deletePurchaseById(jsonToObject<Purchase>(i.body.toString()).id)
            }
        }

        @Test
        fun `get purchase by User Id, user doesn't exist, returns 404 response`() {
            assertEquals(404, retrievePurchasesByUserId(nonExistingUserId).status)
        }

        @Test
        fun `get purchase by User Id, user exist, but purchase don't, returns empty array, 404 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val response = retrievePurchasesByUserId(retrievedUser.id)
            assertEquals(404, response.status)

            userControllerTest.deleteUser(retrievedUser.id)
        }

        @Test
        fun `get purchases by Product Name from the database returns 200 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val addedResponses = addThreePurchases(retrievedUser.id, retrievedProduct.name)

            val response = retrievePurchasesByProductName(retrievedProduct.name)
            assertEquals(200, response.status)
            val retrievedPurchase = jsonNodeToObject<Array<Purchase>>(response)
            assertEquals(3, retrievedPurchase.size)

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            for (i in addedResponses) {
                deletePurchaseById(jsonToObject<Purchase>(i.body.toString()).id)
            }
        }

        @Test
        fun `get purchase by Product Name that doesn't exist, returns 404 response`() {
            assertEquals(404, retrievePurchasesByProductName(nonExistingProductName).status)
        }

        @Test
        fun `get purchase by Product, product exist, but purchase don't, returns empty array, 404 response`() {
            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val response = retrievePurchasesByProductName(retrievedProduct.name)
            assertEquals(404, response.status)

            productControllerTest.deleteProductByName(retrievedProduct.name)
        }

        @Test
        fun `get purchases where price are less, from the database returns 200 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val addedResponses = addThreePurchases(retrievedUser.id, retrievedProduct.name)

            val response = Unirest.get("$origin/api/purchases/${testingPrice}/less").asJson()
            val retrievedPurchase = jsonNodeToObject<Array<Purchase>>(response)

            if (retrievedPurchase.isNotEmpty())
                assertEquals(200, response.status)
            else assertEquals(404, response.status)

            for (j in retrievedPurchase) {
                assertEquals(true, j.price < testingPrice)
            }

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            for (i in addedResponses) {
                deletePurchaseById(jsonToObject<Purchase>(i.body.toString()).id)
            }
        }

        @Test
        fun `get purchases where price are less, returns 404 response`() {
            val response = Unirest.get("$origin/api/purchases/${minimumPrice}/less").asJson()
            assertEquals(404, response.status)
        }

        @Test
        fun `get purchases where price are greater, from the database returns 200 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val addedResponses = addThreePurchases(retrievedUser.id, retrievedProduct.name)

            val response = Unirest.get("$origin/api/purchases/${testingPrice}/greater").asJson()
            val retrievedPurchase = jsonNodeToObject<Array<Purchase>>(response)

            if (retrievedPurchase.isNotEmpty())
                assertEquals(200, response.status)
            else assertEquals(404, response.status)

            for (j in retrievedPurchase) {
                assertEquals(true, j.price > testingPrice)
            }

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            for (i in addedResponses) {
                deletePurchaseById(jsonToObject<Purchase>(i.body.toString()).id)
            }
        }

        @Test
        fun `get purchases where price are greater, returns 404 response`() {
            val response = Unirest.get("$origin/api/purchases/${maximumPrice}/greater").asJson()
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class UpdatePurchases {

        @Test
        fun `update purchase by Id that exists, returns 204 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val purchaseResponse = addPurchase(retrievedUser.id, retrievedProduct.name, testingPrice)
            assertEquals(201, purchaseResponse.status)
            val retrievedPurchase = jsonToObject<Purchase>(purchaseResponse.body.toString())

            val updateResponse = updatePurchase(retrievedPurchase.id, retrievedUser.id, retrievedProduct.name, newPrice)
            assertEquals(204, updateResponse.status)
            val response = retrievePurchaseById(retrievedPurchase.id)
            assertEquals(200, response.status)
            val retrievedResponse = jsonToObject<Purchase>(response.body.toString())
            assertEquals(newPrice, retrievedResponse.price)

            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
            deletePurchaseById(retrievedPurchase.id)
        }

        @Test
        fun `update purchase by Id that doesn't exist, returns 404 response`() {
            val response = updatePurchase(nonExistingId, nonExistingUserId, nonExistingProductName, testingPrice)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class DeletePurchase {

        @Test
        fun `delete purchase by Id that exists, returns 204 response`() {
            val userResponse = userControllerTest.addUser(validName, validEmail)
            assertEquals(201, userResponse.status)
            val retrievedUser = jsonToObject<User>(userResponse.body.toString())

            val productResponse = productControllerTest.addProduct(
                testName,
                productCalories,
                productProteins,
                productFats,
                productCarbohydrates
            )
            assertEquals(201, productResponse.status)
            val retrievedProduct = jsonToObject<Product>(productResponse.body.toString())

            val purchaseResponse = addPurchase(retrievedUser.id, retrievedProduct.name, testingPrice)
            assertEquals(201, purchaseResponse.status)
            val retrievedPurchase = jsonToObject<Purchase>(purchaseResponse.body.toString())

            assertEquals(204, deletePurchaseById(retrievedPurchase.id).status)
            userControllerTest.deleteUser(retrievedUser.id)
            productControllerTest.deleteProductByName(retrievedProduct.name)
        }

        @Test
        fun `delete purchase by Id that doesn't exist, returns 404 response`() {
            val response = deletePurchaseById(nonExistingId)
            assertEquals(404, response.status)
        }
    }
}