package setu.controllers

import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import setu.config.DBConfig
import setu.domain.Dish
import setu.domain.Product
import setu.helpers.*
import setu.utils.jsonNodeToObject
import setu.utils.jsonToObject

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DishControllerTest {

    private val db = DBConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    private val productControllerTest = ProductControllerTest()

    private fun retrieveAllDishes(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/dishes").asJson()
    }

    private fun retrieveDishByName(name: String): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/dishes/${name}").asJson()
    }

    private fun retrieveDishByIngredient(ingredient: String): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/dishes/ingredient/${ingredient}").asJson()
    }

    private fun deleteDishByName(name: String): HttpResponse<String> {
        return Unirest.delete("$origin/api/dishes/$name").asString()
    }

    private fun updateDish(
        name: String, ingredient: String, weight: Int, calories: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/dishes/$name")
            .body(
                """
                {
                  "name":"$name",
                  "ingredient":"$ingredient",
                  "weight":"$weight",
                  "calories":"$calories"
                }
            """.trimIndent()
            ).asJson()
    }

    private fun addDish(
        name: String, ingredient: String, weight: Int, calories: Int
    ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/dishes")
            .body(
                """
                {
                  "name":"$name",
                  "ingredient":"$ingredient",
                  "weight":"$weight",
                  "calories":"$calories"
                }
            """.trimIndent()
            ).asJson()
    }

    private fun setDishes(ingredient: String): List<HttpResponse<JsonNode>> {
        val firstDish = addDish(dishName1, ingredient, dishWeight, dishCalories)
        val secondDish = addDish(dishName2, ingredient, dishWeight, dishCalories)
        return listOf(firstDish, secondDish)
    }

    private fun addProduct(): Product = jsonToObject(
        productControllerTest.addProduct(
            products[0].name,
            products[0].calories,
            products[0].proteins,
            products[0].fats,
            products[0].carbohydrates,
        ).body.toString()
    )

    private fun deleteProduct(name: String) = productControllerTest.deleteProductByName(name).status

    @Nested
    inner class ReadDishes {

        @Test
        fun `get all dishes from the database returns 200 or 404 response`() {
            val response = retrieveAllDishes()
            if (response.status == 200) {
                val retrievedDishes = jsonNodeToObject<Array<Dish>>(response)
                Assertions.assertNotEquals(0, retrievedDishes.size)
            } else {
                Assertions.assertEquals(404, response.status)
            }
        }

        @Test
        fun `get dish by name, returns 200 response`() {
            val addedProduct: Product = addProduct()

            val dishes = setDishes(addedProduct.name)

            for (i in dishes) {
                val retrievedDish: Dish = jsonToObject(i.body.toString())

                val response = retrieveDishByName(retrievedDish.name)
                Assertions.assertEquals(200, response.status)

                Assertions.assertEquals(204, deleteDishByName(retrievedDish.name).status)
            }

            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }

        @Test
        fun `get dish by non-existent name, returns 404 response`() {
            val addedProduct: Product = addProduct()

            val response = retrieveDishByName(unexistingDishName)
            Assertions.assertEquals(404, response.status)

            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }

        @Test
        fun `get dish by ingredient, returns 200 response`() {
            val addedProduct: Product = addProduct()

            val dishes = setDishes(addedProduct.name)

            for (i in dishes) {
                val retrievedDish: Dish = jsonToObject(i.body.toString())

                val response = retrieveDishByIngredient(retrievedDish.ingredient)
                Assertions.assertEquals(200, response.status)

                Assertions.assertEquals(204, deleteDishByName(retrievedDish.name).status)
            }

            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }

        @Test
        fun `get dish by non-existent ingredient, returns 404 response`() {
            val addedProduct: Product = addProduct()

            val response = retrieveDishByName(nonexistingIngredientName)
            Assertions.assertEquals(404, response.status)

            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }
    }

    @Nested
    inner class UpdateDishes {

        @Test
        fun `updating a dish by name, returns 204 response`() {
            val addedProduct: Product = addProduct()

            val dishes = setDishes(addedProduct.name)

            for (i in dishes) {
                val retrievedDish: Dish = jsonToObject(i.body.toString())

                val response = updateDish(retrievedDish.name, retrievedDish.ingredient, newDishWeight, newDishCalories)
                Assertions.assertEquals(204, response.status)

                Assertions.assertEquals(204, deleteDishByName(retrievedDish.name).status)
            }
            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }

        @Test
        fun `updating a dish by non-existed name, returns 404 response`() {
            val addedProduct: Product = addProduct()

            val dishes = setDishes(addedProduct.name)

            for (i in dishes) {
                val retrievedDish: Dish = jsonToObject(i.body.toString())

                val response = updateDish(unexistingDishName, retrievedDish.ingredient, newDishWeight, newDishCalories)
                Assertions.assertEquals(404, response.status)

                Assertions.assertEquals(204, deleteDishByName(retrievedDish.name).status)
            }
            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }
    }

    @Nested
    inner class DeleteDishes {

        @Test
        fun `deleting a dish by name, returns a 204 response`() {
            val addedProduct: Product = addProduct()

            val dishes = setDishes(addedProduct.name)

            for (i in dishes) {
                val retrievedDish: Dish = jsonToObject(i.body.toString())
                Assertions.assertEquals(204, deleteDishByName(retrievedDish.name).status)
            }
            Assertions.assertEquals(204, deleteProduct(addedProduct.name))
        }

        @Test
        fun `deleting a dish by name when it doesn't exist, returns a 404 response`() {
            Assertions.assertEquals(404, deleteDishByName(unexistingDishName).status)
        }
    }
}