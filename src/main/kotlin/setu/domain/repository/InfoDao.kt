package setu.domain.repository

import setu.domain.*

class InfoDao {

    fun getAll(userId: Int): ArrayList<MainDataClass>? {
        val arrayList = arrayListOf<MainDataClass>()

        val user: User? = UserDao().findById(userId)

        return if (user != null) {
            arrayList.add(user)

            val activities: ArrayList<Activity> = ActivityDao().findByUserId(userId)
            arrayList.addAll(activities)

            val sleeps: ArrayList<Sleep> = SleepDao().findByUserId(userId)
            arrayList.addAll(sleeps)

            val purchases: ArrayList<Purchase> = PurchaseDao().findByUserId(userId)
            arrayList.addAll(purchases)

            arrayList
        } else null
    }

    fun getInfoWithDate(userId: Int, year: Int, month: Int): ArrayList<MainDataClass>? {
        val arrayList = arrayListOf<MainDataClass>()

        val user: User? = UserDao().findById(userId)

        return if (user != null) {
            arrayList.add(user)

            val activities: List<Activity> = ActivityDao().findByUserId(userId)
                .filter { it.started.year == year }
                .filter { it.started.monthOfYear == month }
            arrayList.addAll(activities)

            val sleeps: List<Sleep> = SleepDao().findByUserId(userId)
                .filter { it.date.year == year }
                .filter { it.date.monthOfYear == month }
            arrayList.addAll(sleeps)

            val purchases: List<Purchase> = PurchaseDao().findByUserId(userId)
                .filter { it.date.year == year }
                .filter { it.date.monthOfYear == month }
            arrayList.addAll(purchases)

            arrayList
        } else null
    }
}