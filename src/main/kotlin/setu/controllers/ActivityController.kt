package setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import io.javalin.http.Context
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import setu.domain.Activity
import setu.domain.repository.ActivityDao
import com.fasterxml.jackson.datatype.joda.JodaModule

object ActivityController {

    private val activityDao = ActivityDao()

    fun getAllActivities(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(activityDao.getAll()))
    }

    fun getActivitiesByUserId(ctx: Context) {
        val activities = activityDao.findByUserId(ctx.pathParam("user-id").toInt())
        if (activities.isNotEmpty()) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activities))
        } else
            ctx.json("404")
    }

    fun addActivity(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDao.save(activity)
    }

    fun deleteActivitiesByUserId(ctx: Context) {
        if (activityDao.findByUserId(ctx.pathParam("user-id").toInt()).isNotEmpty())
            activityDao.deleteByUserId(ctx.pathParam("user-id").toInt())
        else
            ctx.json("404")
    }

    fun getActivityById(ctx: Context) {
        val activities = activityDao.findById(ctx.pathParam("activity-id").toInt())
        if (activities != null) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activities))
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
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        if (activityDao.findById(ctx.pathParam("activity-id").toInt()) != null) {
            activityDao.update(ctx.pathParam("activity-id").toInt(), activity)
            ctx.json("200")
        } else {
            ctx.json("404")
        }
    }
}