package setu.controllers

import io.javalin.http.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import setu.domain.User
import setu.domain.repository.UserDAO

object HealthTrackerController {

    private val userDao = UserDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context){
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
        }
    }

    fun deleteUserByUserId(ctx: Context){
        userDao.delete(ctx.pathParam("user-id").toInt())
    }

    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.update(
            id = ctx.pathParam("user-id").toInt(),
            user = user
        )
    }

    fun getActivitiesByUserId(ctx: Context){}
    fun deleteActivitiesByUserId(ctx: Context){}
    fun getActivityById(ctx: Context){}
    fun deleteActivityById(ctx: Context){}
    fun updateActivityById(ctx: Context){}
    fun getAllActivities(ctx: Context){}
    fun addActivity(ctx: Context){}

}