package com.rmulka.bs.config

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.net.URI
import javax.sql.DataSource

@Configuration
@Profile("prod")
class DataSourceConfig {

    @Bean
    fun dataSource(): DataSource {
        val dbUri = URI(System.getenv("DATABASE_URL"))
        val username = dbUri.userInfo.split(":")[0]
        val password = dbUri.userInfo.split(":")[1]
        val dbUrl = "jdbc:postgresql://" + dbUri.host + ':' + dbUri.port + dbUri.path

        return DataSourceBuilder.create().let {
            it.url(dbUrl)
            it.username(username)
            it.password(password)
        }.build()
    }
}