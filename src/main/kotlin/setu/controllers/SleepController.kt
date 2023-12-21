package setu.controllers

import io.javalin.http.Context
import setu.domain.Sleep
import setu.domain.repository.SleepDao
import setu.utils.jsonToObject

object SleepController {

    private val sleepDao = SleepDao()

    fun getAllSleeps(ctx: Context) {
        val sleeps = sleepDao.getAll()
        if (sleeps.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(sleeps)
    }

    fun getSleepById(ctx: Context) {
        val sleep = sleepDao.findById(ctx.pathParam("sleep-id").toInt())
        if (sleep != null) {
            ctx.json(sleep)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getSleepsByUserId(ctx: Context) {
        val sleep = sleepDao.findByUserId(ctx.pathParam("user-id").toInt())
        if (sleep.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(sleep)
    }

    fun getSleepsByIdByYear(ctx: Context) {
        val sleeps = sleepDao.findByUserIdByYear(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt()
        )
        if (sleeps.isNotEmpty()) {
            ctx.json(sleeps)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getSleepsByIdByYearAndMonth(ctx: Context) {
        val sleeps = sleepDao.findByUserIdByYearMonth(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt(),
            ctx.pathParam("month").toInt(),
        )
        if (sleeps.isNotEmpty()) {
            ctx.json(sleeps)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addSleep(ctx: Context) {
        val sleep: Sleep = jsonToObject(ctx.body())
        val sleepId = sleepDao.save(sleep)
        sleep.id = sleepId
        ctx.json(sleep)
        ctx.status(201)
    }

    fun deleteSleep(ctx: Context) {
        if (sleepDao.delete(ctx.pathParam("sleep-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateSleep(ctx: Context) {
        val foundSleep: Sleep = jsonToObject(ctx.body())
        if ((sleepDao.update(id = ctx.pathParam("sleep-id").toInt(), sleep = foundSleep)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}