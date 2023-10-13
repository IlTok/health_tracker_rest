package setu.config

import org.jetbrains.exposed.sql.Database

class DBConfig{

    fun getDbConnection() :Database{

        val PGHOST = "flora.db.elephantsql.com"
        val PGPORT = "5432"
        val PGUSER = "lwclufib"
        val PGPASSWORD = "u_VeSGoNJX4lrMjnLjilRoXpoXyh88qX"
        val PGDATABASE = "lwclufib"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        val dbConfig = Database.connect(
            url = dbUrl,
            driver="org.postgresql.Driver",
            user = PGUSER,
            password = PGPASSWORD
        )

        return dbConfig
    }

}