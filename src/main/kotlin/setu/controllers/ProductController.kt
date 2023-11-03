package setu.controllers

import io.javalin.http.Context
import setu.domain.Product
import setu.domain.repository.ProductDao
import setu.utils.jsonToObject

object ProductController {

    private val productDao = ProductDao()

    fun getAllProducts(ctx: Context) {
        val products = productDao.getAll()
        if (products.isNotEmpty()) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(products)
    }

    fun getProductByName(ctx: Context) {
        val product = productDao.findByName(ctx.pathParam("product-name"))
        if (product != null) {
            ctx.json(product)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addProduct(ctx: Context) {
        val product: Product = jsonToObject(ctx.body())
        if (productDao.findByName(product.name) == null) {
            productDao.save(product)
            ctx.json(product)
            ctx.status(201)
        } else {
            ctx.status(409)
        }
    }

    fun deleteProduct(ctx: Context){
        if (productDao.delete(ctx.pathParam("product-name")) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateProduct(ctx: Context){
        val newProduct : Product = jsonToObject(ctx.body())
        if ((productDao.update(name = ctx.pathParam("product-name"), product = newProduct)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}