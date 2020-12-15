package com.rmulka.bs.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration("flyway")
class FlywayConfig(@Qualifier("dataSource") private val dataSource: DataSource) {

    @PostConstruct
    fun runFlyway() {
        val flyway = Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .locations("classpath:db/migration")
                .schemas("bs")
                .load()
        flyway.migrate()
    }
}