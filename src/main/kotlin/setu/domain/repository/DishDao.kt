package setu.domain.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.Dish
import setu.domain.db.Dishes
import setu.utils.mapToDish

class DishDao {

    fun getAll(): ArrayList<Dish> {
        val dishList: ArrayList<Dish> = arrayListOf()
        transaction {
            Dishes.selectAll().map {
                dishList.add(mapToDish(it))
            }
        }
        return dishList
    }

    fun findByName(name: String): Dish? {
        return transaction {
            Dishes.select {
                Dishes.name eq name
            }
                .map { mapToDish(it) }
                .firstOrNull()
        }
    }

    fun save(dish: Dish) {
        return transaction {
            Dishes.insert {
                it[name] = dish.name
                it[ingredient] = dish.ingredient
                it[weight] = dish.weight
                it[calories] = dish.calories
            }
        }
    }

    fun deleteByName(name: String): Int {
        return transaction {
            Dishes.deleteWhere {
                Dishes.name eq name
            }
        }
    }

    fun updateByName(name: String, dish: Dish): Int {
        return transaction {
            Dishes.update({
                Dishes.name eq name
            }) {
                it[ingredient] = dish.ingredient
                it[weight] = dish.weight
                it[calories] = dish.calories
            }
        }
    }
}