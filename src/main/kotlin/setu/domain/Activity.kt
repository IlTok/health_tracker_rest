package setu.domain

data class Activity(
    var id: Int,
    var description: String,
    var duration: Int,
    var calories: Int,
    var started: Boolean,
    var userId: Int,
) {
}