package setu.domain.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.Sleep
import setu.domain.db.Sleeps
import setu.domain.db.Users
import setu.utils.mapToSleep

class SleepDao {

    fun getAll(): ArrayList<Sleep> {
        val sleepList: ArrayList<Sleep> = arrayListOf()
        transaction {
            Users.selectAll().map {
                sleepList.add(mapToSleep(it))
            }
        }
        return sleepList
    }

    fun findById(id: Int): Sleep? {
        return transaction {
            Sleeps.select {
                Sleeps.id eq id
            }
                .map { mapToSleep(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): ArrayList<Sleep> {
        val sleepList: ArrayList<Sleep> = arrayListOf()
        transaction {
            Users.select {
                Sleeps.userId eq userId
            }.map {
                sleepList.add(mapToSleep(it))
            }
        }
        return sleepList
    }

    fun save(sleep: Sleep): Int {
        return transaction {
            Sleeps.insert {
                it[duration] = sleep.duration
                it[date] = sleep.date
                it[userId] = sleep.userId
            } get Users.id
        }
    }

    fun delete(id: Int): Int {
        return transaction {
            Sleeps.deleteWhere {
                Sleeps.id eq id
            }
        }
    }

    fun update(id: Int, sleep: Sleep): Int {
        return transaction {
            Sleeps.update({
                Sleeps.id eq id
            }) {
                it[duration] = sleep.duration
                it[date] = sleep.date
                it[userId] = sleep.userId
            }
        }
    }
}