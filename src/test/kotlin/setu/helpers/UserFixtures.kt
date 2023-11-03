package setu.helpers

import setu.domain.User

const val nonExistingEmail = "thisemaildoesntexist@gmuil.cim"
const val validName = "Test User 1"
const val validEmail = "testuser1@test.com"
const val updatedName = "Updated Name"
const val updatedEmail = "Updated Email"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)