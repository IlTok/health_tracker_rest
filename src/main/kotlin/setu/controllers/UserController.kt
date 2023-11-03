package setu.controllers

import io.javalin.http.Context
import setu.domain.User
import setu.domain.repository.UserDao
import setu.utils.jsonToObject

object UserController {

    private val userDao = UserDao()

    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(users)
    }

    fun getUserById(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addUser(ctx: Context) {
        val user : User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        user.id = userId
        ctx.json(user)
        ctx.status(201)
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else
            ctx.status(404)
    }

    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateUser(ctx: Context){
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}