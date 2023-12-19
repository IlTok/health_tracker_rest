package setu.domain.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.Purchase
import setu.domain.db.Purchases
import setu.utils.mapToPurchase

class PurchaseDao {

    fun getAll(): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = arrayListOf()
        transaction {
            Purchases.selectAll().map {
                purchaseList.add(mapToPurchase(it))
            }
        }
        return purchaseList
    }

    fun findById(id: Int): Purchase? {
        return transaction {
            Purchases.select() {
                Purchases.id eq id
            }
                .map { mapToPurchase(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = arrayListOf()
        transaction {
            Purchases.select() {
                Purchases.userId eq userId
            }
                .map { purchaseList.add(mapToPurchase(it)) }
                .firstOrNull()
        }
        return purchaseList
    }

    fun findByProductName(productName: String): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = arrayListOf()
        transaction {
            Purchases.select() {
                Purchases.productName eq productName
            }
                .map { purchaseList.add(mapToPurchase(it)) }
                .firstOrNull()
        }
        return purchaseList
    }

    fun findPurchasesWherePriceGreaterThan(price: Double): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = arrayListOf()
        transaction {
            Purchases
                .select { Purchases.price greater price }
                .map { purchaseList.add(mapToPurchase(it)) }
        }
        return purchaseList
    }

    fun findPurchasesWherePriceLessThan(price: Double): ArrayList<Purchase> {
        val purchaseList: ArrayList<Purchase> = arrayListOf()
        transaction {
            Purchases
                .select { Purchases.price less  price }
                .map { purchaseList.add(mapToPurchase(it)) }
        }
        return purchaseList
    }

    fun save(purchase: Purchase): Int {
        return transaction {
            Purchases.insert {
                it[userId] = purchase.userId
                it[productName] = purchase.productName
                it[price] = purchase.price
                it[date] = purchase.date
            } get Purchases.id
        }
    }

    fun updateById(id: Int, purchase: Purchase): Int {
        return transaction {
            Purchases.update({
                Purchases.id eq id
            }) {
                it[userId] = purchase.userId
                it[productName] = purchase.productName
                it[price] = purchase.price
                it[date] = purchase.date
            }
        }
    }

    fun deleteById(id: Int): Int {
        return transaction {
            Purchases.deleteWhere {
                Purchases.id eq id
            }
        }
    }

}