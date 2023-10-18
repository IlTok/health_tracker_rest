package setu.controllers

import com.fasterxml.jackson.module.kotlin.readValue
import io.javalin.http.Context
import setu.domain.Activity
import setu.domain.repository.ActivityDao
import setu.utils.getConfigureMapper

object ActivityController {

    private val activityDao = ActivityDao()

    fun getAllActivities(ctx: Context) {
        ctx.json(getConfigureMapper().writeValueAsString(activityDao.getAll()))
    }

    fun getActivitiesByUserId(ctx: Context) {
        val activities = activityDao.findByUserId(ctx.pathParam("user-id").toInt())
        if (!activities.isNullOrEmpty()) {
            ctx.json(getConfigureMapper().writeValueAsString(activities))
        } else
            ctx.json("404")
    }

    fun addActivity(ctx: Context) {
        val activity = getConfigureMapper().readValue<Activity>(ctx.body())
        activityDao.save(activity)
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (!activityDao.findByUserId(ctx.pathParam("user-id").toInt()).isNullOrEmpty())
            activityDao.deleteByUserId(ctx.pathParam("user-id").toInt())
        else
            ctx.json("404")
    }

    fun getActivityById(ctx: Context) {
        val activities = activityDao.findById(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
            ctx.json(getConfigureMapper().writeValueAsString(activities))
        } else {
            ctx.json("404")
        }
    }

    fun deleteActivityById(ctx: Context) {
        if (activityDao.findById(ctx.pathParam("activity-id").toInt()) != null) {
            activityDao.deleteById(ctx.pathParam("activity-id").toInt())
            ctx.json("200")
        } else {
            ctx.json("404")
        }
    }

    fun updateActivityById(ctx: Context) {
        val activity = getConfigureMapper().readValue<Activity>(ctx.body())
        if (activityDao.findById(ctx.pathParam("activity-id").toInt()) != null) {
            activityDao.update(ctx.pathParam("activity-id").toInt(), activity)
            ctx.json("200")
        } else {
            ctx.json("404")
        }
    }
}