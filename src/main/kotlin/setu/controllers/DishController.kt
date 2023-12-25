package setu.controllers

import io.javalin.http.Context
import setu.domain.Dish
import setu.domain.repository.DishDao
import setu.utils.jsonToObject

object DishController {

    private val dishDao = DishDao()

    fun getAllDishes(ctx: Context) {
        val dishes = dishDao.getAll()
        if (dishes.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(dishes)
    }

    fun getDishByName(ctx: Context) {
        val dish = dishDao.findByName(ctx.pathParam("dish-name"))
        if (dish != null) {
            ctx.json(dish)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addDish(ctx: Context) {
        val dish: Dish = jsonToObject(ctx.body())
        if (dishDao.findByName(dish.name) == null) {
            dishDao.save(dish)
            ctx.json(dish)
            ctx.status(201)
        } else {
            ctx.status(409)
        }
    }

    fun deleteDish(ctx: Context) {
        if (dishDao.deleteByName(ctx.pathParam("dish-name")) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateDish(ctx: Context) {
        val newDish: Dish = jsonToObject(ctx.body())
        if ((dishDao.updateByName(
                name = ctx.pathParam("dish-name"),
                dish = newDish
            )) != 0
        )
            ctx.status(204)
        else
            ctx.status(404)
    }
}