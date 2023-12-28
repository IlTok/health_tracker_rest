package setu.helpers

import setu.domain.User

const val nonExistingEmail = "thisemaildoesntexist@gmuil.cim"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland test name 1", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat test name 2", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary test name 3", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer test name 4", email = "carol@singer.com", id = 4)
)