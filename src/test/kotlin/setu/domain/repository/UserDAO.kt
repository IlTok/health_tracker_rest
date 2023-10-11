package setu.domain.repository

import setu.domain.User

class UserDAO {

    private val users = arrayListOf(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun getAll() : ArrayList<User>{
        return users
    }

    fun findById(id: Int): User? {
        return users.find {
            it.id == id
        }
    }

    fun findByEmail(email: String) :User?{
        return users.find {
            it.email == email
        }
    }

    fun save(user: User){
        users.add(user)
    }

    fun delete(id: Int){
        val userById = users.find { it.id == id }
        users.remove(userById)
    }

    fun update(id: Int, user: User){
        val foundUser = findById(id)
        foundUser?.email = user.email
        foundUser?.name = user.name
        foundUser?.id = user.id
    }

}