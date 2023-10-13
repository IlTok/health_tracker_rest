package setu.domain.repository

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import setu.domain.User
import setu.domain.db.Users
import setu.utils.mapToUser

class UserDAO {

    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    fun findById(id: Int): User? {
        return transaction {
            Users.select() {
                Users.id eq id
            }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select() {
                Users.email eq email
            }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }

    fun save(user: User) {
//        users.add(user)
    }

    fun delete(id: Int) {
//        val userById = users.find { it.id == id }
//        users.remove(userById)
    }

    fun update(id: Int, user: User) {
//        val foundUser = findById(id)
//        foundUser?.email = user.email
//        foundUser?.name = user.name
//        foundUser?.id = user.id
    }

}