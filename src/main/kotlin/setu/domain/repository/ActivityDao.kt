package setu.domain.repository

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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

    fun findByUserId(userDd: Int): Activity? {
        return transaction {
            Activities.select() {
                Activities.userId eq userDd
            }
                .map { mapToActivity(it) }
                .firstOrNull()
        }
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
}