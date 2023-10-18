package setu.controllers

import io.javalin.http.Context
import com.fasterxml.jackson.module.kotlin.readValue
import setu.domain.User
import setu.domain.repository.UserDao
import setu.utils.getMapper

object UserController {

    private val userDao = UserDao()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserById(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null)
            ctx.json(user)
        else
            ctx.json("404")
    }

    fun addUser(ctx: Context) {
        val user = getMapper().readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null)
            ctx.json(user)
        else
            ctx.json("404")
    }

    fun deleteUserById(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null)
            userDao.delete(ctx.pathParam("user-id").toInt())
        else
            ctx.json("404")
    }

    fun updateUser(ctx: Context) {
        val user = getMapper().readValue<User>(ctx.body())
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null)
            userDao.update(
                id = ctx.pathParam("user-id").toInt(),
                user = user
            )
        else
            ctx.json("404")
    }
}