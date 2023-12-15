package setu.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent
import setu.controllers.*
import setu.utils.jsonObjectMapper

class JavalinConfig {

    val app: Javalin = Javalin.create{
        //added this jsonMapper for our integration tests - serialise objects to json
        it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        it.staticFiles.enableWebjars()
        it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
    }.apply {
        exception(Exception::class.java) { e, _ -> e.printStackTrace() }
        error(404) { ctx -> ctx.json("404 : Not Found") }
    }

    fun startJavalinService(): Javalin {
        app.start(getRemoteAssignedPort())
        registerRoutes(app)
        return app
    }

    fun getJavalinService(): Javalin {
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
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))

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
                path("{price}"){
                    path("/less") {
                        get(PurchaseController::getPurchasesWherePricesLessThan)
                    }
                    path("/greater") {
                        get(PurchaseController::getPurchasesWherePricesGreaterThan)
                    }
                }
                path("{purchase-id}"){
                    get(PurchaseController::getPurchaseById)
                    patch(PurchaseController::updatePurchaseById)
                    delete(PurchaseController::deletePurchaseById)
                }
                path("/user/{user-id}"){
                    get(PurchaseController::getPurchasesByUserId)
                }
                path("/product/{product-name}"){
                    get(PurchaseController::getPurchasesByProductName)
                }
            }
            path("/api/sleeps"){
                get(SleepController::getAllSleeps)
                post(SleepController::addSleep)
                path("{user-id}"){
                    get(SleepController::getSleepsByUserId)
                    patch(SleepController::updateSleep)
                }
                path("{sleep-id}"){
                    get(SleepController::getSleepById)
                    delete(SleepController::deleteSleep)
                }
            }
        }
    }
}