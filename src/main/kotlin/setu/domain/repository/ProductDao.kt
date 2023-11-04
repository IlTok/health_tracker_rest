package setu.domain.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.Product
import setu.domain.db.Products
import setu.utils.mapToProduct

class ProductDao {

    fun getAll(): ArrayList<Product> {
        val productList: ArrayList<Product> = arrayListOf()
        transaction {
            Products.selectAll().map {
                productList.add(mapToProduct(it))
            }
        }
        return productList
    }

    fun findByName(name: String): Product? {
        return transaction {
            Products.select() {
                Products.name eq name
            }
                .map { mapToProduct(it) }
                .firstOrNull()
        }
    }

    fun save(product: Product) {
        return transaction {
            Products.insert {
                it[name] = product.name
                it[calories] = product.calories
                it[proteins] = product.proteins
                it[fats] = product.fats
                it[carbohydrates] = product.carbohydrates
            }
        }
    }

    fun deleteByName(name: String): Int {
        return transaction {
            Products.deleteWhere {
                Products.name eq name
            }
        }
    }

    fun updateByName(name: String, product: Product): Int {
        return transaction {
            Products.update({
                Products.name eq name
            }) {
                it[calories] = product.calories
                it[proteins] = product.proteins
                it[fats] = product.fats
                it[carbohydrates] = product.carbohydrates
            }
        }
    }
}