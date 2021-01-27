package com.rmulka.bs.config

import org.jooq.SQLDialect
import org.jooq.conf.Settings
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Component
@Configuration
class JooqConfig(private val dataSource: DataSource) {

    @Bean
    fun connectionProvider() = DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))

    @Bean
    fun dsl() = DefaultDSLContext(configuration())

    fun configuration(): DefaultConfiguration {
        return DefaultConfiguration()
                .set(connectionProvider())
                .set(SQLDialect.POSTGRES)
                .set(Settings()
                        .withExecuteWithOptimisticLocking(true)
                        .withExecuteWithOptimisticLockingExcludeUnversioned(true)) as DefaultConfiguration
    }
}