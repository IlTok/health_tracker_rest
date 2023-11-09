package setu.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import setu.config.DBConfig
import setu.domain.Product
import setu.helpers.*
import setu.utils.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    fun addProduct(
        name: String, calories: Int, proteins: Int,
        fats: Int, carbohydrates: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/products")
            .body(
                """
                {
                   "name":"$name",
                   "calories":$calories,
                   "proteins":$proteins,
                   "fats":$fats,
                   "carbohydrates":$carbohydrates
                }
            """.trimIndent()
            )
            .asJson()
    }

    fun deleteProductByName(name: String): HttpResponse<String> {
        return Unirest.delete("$origin/api/products/${name}").asString()
    }

    private fun retrieveProductByName(name: String): HttpResponse<String> {
        return Unirest.get(origin + "/api/products/${name}").asString()
    }

    private fun updateProductByName(
        name: String, calories: Int, proteins: Int,
        fats: Int, carbohydrates: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/products/${name}")
            .body(
                """{
                   "name":"$name",
                   "calories":$calories,
                   "proteins":$proteins,
                   "fats":$fats,
                   "carbohydrates":$carbohydrates
                }
            """.trimIndent()
            )
            .asJson()
    }

    private fun addResponse() = addProduct(
        productName,
        productCalories,
        productProteins,
        productFats,
        productCarbohydrates
    )

    @Nested
    inner class GetProducts {

        @Test
        fun `get all products from the database returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/products/").asString()
            if (response.status == 200) {
                val retrievedProducts: ArrayList<Product> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedProducts.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get product by name when user does not exist returns 404 response`() {
            val retrieveResponse = Unirest.get(origin + "/api/products/${notExistingProductName}").asString()
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a product by name when name exists, returns a 200 response`() {
            val addedResponse = addResponse()
            val addedProduct: Product = jsonToObject(addedResponse.body.toString())
            val retrieveResponse = retrieveProductByName(addedProduct.name)
            assertEquals(200, retrieveResponse.status)
            deleteProductByName(addedProduct.name)
        }

        @Test
        fun `getting a product where calories are greater, returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/products/calories/greater/${caloriesThan}").asString()
            if (response.status == 200) {
                val retrievedProducts: ArrayList<Product> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedProducts.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `getting a product where calories are less, returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/products/calories/less/${caloriesThan}").asString()
            if (response.status == 200) {
                val retrievedProducts: ArrayList<Product> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedProducts.size)
            } else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `getting a product where calories are greater and not equal, returns 200 or 404 response`() {
            val response1 = Unirest.get("$origin/api/products/calories/greater/${caloriesEquals}").asString()
            if (response1.status == 200) {
                val retrievedProducts: ArrayList<Product> = jsonToObject(response1.body.toString())
                assertNotEquals(0, retrievedProducts.size)

                val initialSize = retrievedProducts.size

                val addedResponse = addResponse()
                val addedProduct: Product = jsonToObject(addedResponse.body.toString())
                assertEquals(201, addedResponse.status)

                val response2 = Unirest.get("$origin/api/products/calories/greater/${caloriesEquals}").asString()
                val responseProducts: ArrayList<Product> = jsonToObject(response2.body.toString())
                assertEquals(initialSize, responseProducts.size)

                deleteProductByName(addedProduct.name)
            } else {
                assertEquals(404, response1.status)

                val addedResponse = addResponse()
                val addedProduct: Product = jsonToObject(addedResponse.body.toString())
                assertEquals(201, addedResponse.status)

                val response3 = Unirest.get("$origin/api/products/calories/greater/${caloriesEquals}").asString()
                assertEquals(404, response3.status)

                deleteProductByName(addedProduct.name)
            }
        }

        @Test
        fun `getting a product where calories are less and not equal, returns 200 or 404 response`() {
            val response1 = Unirest.get("$origin/api/products/calories/less/${caloriesEquals}").asString()
            if (response1.status == 200) {
                val retrievedProducts: ArrayList<Product> = jsonToObject(response1.body.toString())
                assertNotEquals(0, retrievedProducts.size)

                val initialSize = retrievedProducts.size

                val addedResponse = addResponse()
                val addedProduct: Product = jsonToObject(addedResponse.body.toString())
                assertEquals(201, addedResponse.status)

                val response2 = Unirest.get("$origin/api/products/calories/less/${caloriesEquals}").asString()
                val responseProducts: ArrayList<Product> = jsonToObject(response2.body.toString())
                assertEquals(initialSize, responseProducts.size)

                deleteProductByName(addedProduct.name)
            } else {
                assertEquals(404, response1.status)

                val addedResponse = addResponse()
                val addedProduct: Product = jsonToObject(addedResponse.body.toString())
                assertEquals(201, addedResponse.status)

                val response3 = Unirest.get("$origin/api/products/calories/less/${caloriesEquals}").asString()
                assertEquals(404, response3.status)

                deleteProductByName(addedProduct.name)
            }
        }
    }

    @Nested
    inner class CreateProduct {

        @Test
        fun `add a product with correct details returns a 201 response`() {
            val addedResponse = addResponse()
            assertEquals(201, addedResponse.status)
            val retrieveResponse = retrieveProductByName(productName)
            assertEquals(200, retrieveResponse.status)
            val retrievedProduct: Product = jsonToObject(addedResponse.body.toString())
            assertEquals(productName, retrievedProduct.name)
            val deleteResponse = deleteProductByName(productName)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateProduct {

        @Test
        fun `updating a product when it exists, returns a 204 response`() {
            val addedResponse = addResponse()
            val addedProduct: Product = jsonToObject(addedResponse.body.toString())
            assertEquals(
                204, updateProductByName(
                    addedProduct.name,
                    updatedProductCalories,
                    addedProduct.proteins,
                    addedProduct.fats,
                    addedProduct.carbohydrates
                ).status
            )
            val updatedProductResponse = retrieveProductByName(addedProduct.name)
            val updatedProduct: Product = jsonToObject(updatedProductResponse.body.toString())
            assertEquals(updatedProductCalories, updatedProduct.calories)
            deleteProductByName(updatedProduct.name)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {
            assertEquals(
                404, updateProductByName(
                    notExistingProductName,
                    productCalories,
                    productProteins,
                    productFats,
                    productCarbohydrates
                ).status
            )
        }
    }

    @Nested
    inner class DeleteProduct {

        @Test
        fun `deleting a product by name when it doesn't exist, returns a 404 response`() {
            assertEquals(404, deleteProductByName(notExistingProductName).status)
        }

        @Test
        fun `deleting a product by name when it exists, returns a 204 response`() {
            val addedResponse = addResponse()
            val addedProduct: Product = jsonToObject(addedResponse.body.toString())
            assertEquals(200, retrieveProductByName(addedProduct.name).status)
            assertEquals(204, deleteProductByName(addedProduct.name).status)
            assertEquals(404, retrieveProductByName(addedProduct.name).status)
        }
    }

}