package setu.controllers

import io.javalin.http.Context
import setu.domain.Purchase
import setu.domain.repository.ProductDao
import setu.domain.repository.PurchaseDao
import setu.domain.repository.UserDao
import setu.utils.jsonToObject

object PurchaseController {

    private val productDao = ProductDao()
    private val userDao = UserDao()
    private val purchaseDao = PurchaseDao()

    fun getAllPurchases(ctx: Context) {
        val purchases = purchaseDao.getAll()
        if (purchases.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(purchases)
    }

    fun getPurchaseById(ctx: Context) {
        val purchase = purchaseDao.findById(ctx.pathParam("purchase-id").toInt())
        if (purchase != null) {
            ctx.json(purchase)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val purchases = purchaseDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (purchases.isNotEmpty()) {
                ctx.json(purchases)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesByProductName(ctx: Context) {
        if (productDao.findByName(ctx.pathParam("product-name")) != null) {
            val purchases = purchaseDao.findByProductName(ctx.pathParam("product-name"))
            if (purchases.isNotEmpty()) {
                ctx.json(purchases)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesWherePricesGreaterThan(ctx: Context) {
        val purchases = purchaseDao.findPurchasesWherePriceGreaterThan(ctx.pathParam("price").toDouble())
        if (purchases.isNotEmpty()) {
            ctx.json(purchases)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesWherePricesLessThan(ctx: Context) {
        val purchases = purchaseDao.findPurchasesWherePriceLessThan(ctx.pathParam("price").toDouble())
        if (purchases.isNotEmpty()) {
            ctx.json(purchases)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesByIdByYear(ctx: Context) {
        val purchases = purchaseDao.findByUserIdByYear(
            ctx.pathParam("user-id").toInt(),
            ctx.pathParam("year").toInt()
        )
        if (purchases.isNotEmpty()) {
            ctx.json(purchases)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getPurchasesByIdByYearAndMonth(ctx: Context) {
        val activities = purchaseDao.findByUserIdByYearMonth(
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

    fun addPurchase(ctx: Context) {
        val purchase: Purchase = jsonToObject(ctx.body())
        val userId = userDao.findById(purchase.userId)
        val productName = productDao.findByName(purchase.productName)
        if (userId != null && productName != null) {
            val purchaseId = purchaseDao.save(purchase)
            purchase.id = purchaseId
            ctx.json(purchase)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun updatePurchaseById(ctx: Context) {
        val purchase: Purchase = jsonToObject(ctx.body())
        if (purchaseDao.updateById(
                id = ctx.pathParam("purchase-id").toInt(),
                purchase = purchase
            ) != 0
        )
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deletePurchaseById(ctx: Context) {
        if (purchaseDao.deleteById(ctx.pathParam("purchase-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}