package setu.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import setu.controllers.ActivityController
import setu.controllers.UserController

class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create().apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}") {
                    get(UserController::getUserById)
                    delete(UserController::deleteUserById)
                    patch(UserController::updateUser)
                    path("/activities"){
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivitiesByUserId)
                    }
                }
                path("/email/{email}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities"){
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}"){
                    get(ActivityController::getActivityById)
                    delete(ActivityController::deleteActivityById)
                    patch(ActivityController::updateActivityById)
                }
            }
        }
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }
}