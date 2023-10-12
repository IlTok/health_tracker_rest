package setu

import setu.config.DBConfig
import setu.config.JavalinConfig

fun main(){
    DBConfig().getDbConnection()
    JavalinConfig().startJavalinService()
}