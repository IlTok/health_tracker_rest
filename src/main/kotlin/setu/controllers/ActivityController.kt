package setu.controllers

import io.javalin.http.Context
import setu.domain.Activity
import setu.domain.repository.ActivityDao
import setu.domain.repository.UserDao
import setu.utils.jsonToObject

object ActivityController {

    private val activityDao = ActivityDao()
    private val userDao = UserDao()

    fun getAllActivities(ctx: Context) {
        val activities = activityDao.getAll()
        if (activities.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(activities)
    }

    fun getActivitiesByUserId(ctx: Context) {
        val activities = activityDao.findByUserId(ctx.pathParam("user-id").toInt())
        if (activities.isNotEmpty()) {
            ctx.json(activities)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getActivityById(ctx: Context) {
        val activity = activityDao.findById((ctx.pathParam("activity-id").toInt()))
        if (activity != null) {
            ctx.json(activity)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getActivitiesByIdByYear(ctx: Context) {
        val activities = activityDao.findByUserIdByYear(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt()
        )
        if (activities.isNotEmpty()) {
            ctx.json(activities)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getActivitiesByIdByYearAndMonth(ctx: Context) {
        val activities = activityDao.findByUserIdByYearMonth(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt(),
            ctx.pathParam("month").toInt(),
        )
        if (activities.isNotEmpty()) {
            ctx.json(activities)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addActivity(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        val userId = userDao.findById(activity.userId)
        if (userId != null) {
            val activityId = activityDao.save(activity)
            activity.id = activityId
            ctx.json(activity)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun deleteActivityById(ctx: Context) {
        if (activityDao.deleteById(ctx.pathParam("activity-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (activityDao.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateActivityById(ctx: Context) {
        val activity: Activity = jsonToObject(ctx.body())
        if (activityDao.update(
                id = ctx.pathParam("activity-id").toInt(),
                activity = activity
            ) != 0
        )
            ctx.status(204)
        else
            ctx.status(404)
    }
}