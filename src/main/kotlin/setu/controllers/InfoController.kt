package setu.controllers

import io.javalin.http.Context
import setu.domain.repository.InfoDao

object InfoController {

    private val infoDao = InfoDao()

    fun getAllUserInfo(ctx: Context) {
        val info = infoDao.getAll(ctx.pathParam("user-id").toInt())
        if (info != null) {
            ctx.status(200)
            ctx.json(info)
        } else {
            ctx.status(404)
        }
    }

    fun getUserInfoDate(ctx: Context) {
        val dateInfo = infoDao.getInfoWithDate(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt(),
            ctx.pathParam("month").toInt()
        )
        if (dateInfo != null) {
            ctx.status(200)
            ctx.json(dateInfo)
        } else {
            ctx.status(404)
        }
    }
}