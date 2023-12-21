package setu.domain.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.Activity
import setu.domain.db.Activities
import setu.utils.mapToActivity

class ActivityDao {

    fun getAll(): ArrayList<Activity> {
        val activityList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activityList.add(mapToActivity(it))
            }
        }
        return activityList
    }

    fun findByUserId(userId: Int): ArrayList<Activity> {
        val activityList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities
                .select { Activities.userId eq userId }
                .map {
                    activityList.add(mapToActivity(it))
                }
        }
        return activityList
    }

    fun findByUserIdByYear(userId: Int, year: Int)
        = findByUserId(userId).filter {
            it.started.year == year
    }

    fun findByUserIdByYearMonth(userId: Int, year: Int, month: Int)
            = findByUserIdByYear(userId, year).filter {
                it.started.monthOfYear == month
    }

    fun save(activity: Activity): Int {
        return transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
            } get Activities.id
        }
    }

    fun deleteByUserId(userId: Int): Int {
        return transaction {
            Activities.deleteWhere {
                Activities.userId eq userId
            }
        }
    }

    fun findById(id: Int): Activity? {
        return transaction {
            Activities
                .select { Activities.id eq id }
                .map { mapToActivity(it) }
                .firstOrNull()
        }
    }

    fun deleteById(id: Int): Int {
        return transaction {
            Activities.deleteWhere {
                Activities.id eq id
            }
        }
    }

    fun update(id: Int, activity: Activity): Int {
        return transaction {
            Activities.update({
                Activities.id eq id
            }) {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
            }
        }
    }
}