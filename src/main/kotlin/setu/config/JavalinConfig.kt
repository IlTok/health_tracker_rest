package setu.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import setu.controllers.ActivityController
import setu.controllers.UserController
import io.javalin.json.JavalinJackson
import setu.controllers.ProductController
import setu.controllers.PurchaseController
import setu.utils.jsonObjectMapper

class JavalinConfig {

    val app = Javalin.create {
        //added this jsonMapper for our integration tests - serialise objects to json
        it.jsonMapper(JavalinJackson(jsonObjectMapper()))
    }.apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("404 : Not Found") }
    }

    fun startJavalinService(): Javalin {
        app.start(getRemoteAssignedPort())
        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserById)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("/activities") {
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivitiesByUserId)
                    }
                }
                path("/email/{email}") {
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}") {
                    get(ActivityController::getActivityById)
                    delete(ActivityController::deleteActivityById)
                    patch(ActivityController::updateActivityById)
                }
            }
            path("/api/products") {
                get(ProductController::getAllProducts)
                post(ProductController::addProduct)
                path("{product-name}") {
                    get(ProductController::getProductByName)
                    patch(ProductController::updateProduct)
                    delete(ProductController::deleteProduct)
                }
                path("/calories") {
                    path("/greater/{calories}") {
                        get(ProductController::getProductsWhereCaloriesGreaterThan)
                    }
                    path("/less/{calories}") {
                        get(ProductController::getProductsWhereCaloriesLowerThan)
                    }
                }
            }
            path("/api/purchases"){
                get(PurchaseController::getAllPurchases)
                post(PurchaseController::addPurchase)
                path("/user/{user-id}"){
                    get(PurchaseController::getPurchasesByUserId)
                    path("{product-name}"){
                        get(PurchaseController::getPurchasesByProductNameAndUserId)
                    }
                }
                path("/product/{product-name}"){
                    get(PurchaseController::getPurchasesByProductName)
                }
                path("{purchase-id}"){
                    delete(PurchaseController::deletePurchaseById)
                }
            }
        }
    }
}