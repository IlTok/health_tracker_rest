package setu.helpers

import setu.config.JavalinConfig


object ServerContainer {

    val instance by lazy { startServerContainer() }

    private fun startServerContainer() = JavalinConfig().startJavalinService()
}
