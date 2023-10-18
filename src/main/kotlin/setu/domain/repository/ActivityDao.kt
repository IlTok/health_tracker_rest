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

    fun findByUserId(userDd: Int): ArrayList<Activity> {
        val activityList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.select() {
                Activities.userId eq userDd
            }
                .map {
                    activityList.add(mapToActivity(it))
                }
        }
        return activityList
    }

    fun save(activity: Activity) {
        transaction {
            Activities.insert {
                it[description] = activity.description
                it[duration] = activity.duration
                it[calories] = activity.calories
                it[started] = activity.started
                it[userId] = activity.userId
            }
        }
    }

    fun deleteByUserId(userId: Int) {
        transaction {
            Activities.deleteWhere {
                Activities.userId eq userId
            }
        }
    }

    fun findById(id: Int): Activity? {
        return transaction {
            Activities.select() {
                Activities.id eq id
            }
                .map { mapToActivity(it) }
                .firstOrNull()
        }
    }

    fun deleteById(id: Int) {
        transaction {
            Activities.deleteWhere {
                Activities.id eq id
            }
        }
    }

    fun update(id: Int, activity: Activity) {
        transaction {
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