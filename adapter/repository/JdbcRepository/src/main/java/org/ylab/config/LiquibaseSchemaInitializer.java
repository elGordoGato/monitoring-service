package org.ylab.config;

import liquibase.change.DatabaseChange;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@ConditionalOnClass({SpringLiquibase.class, DatabaseChange.class})
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import({LiquibaseSchemaInitializer.SpringLiquibaseDependsOnPostProcessor.class})
public class LiquibaseSchemaInitializer {


    @Component
    @ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", matchIfMissing = true)
    public static class SchemaInitBean implements InitializingBean {

        private static final String CREATE_SCHEMA_QUERY =
                "CREATE SCHEMA IF NOT EXISTS %s;\n";
        private final DataSource dataSource;
        private final String entitiesSchemaName;
        private final String serviceSchemaName;
        private final String auditSchemaName;


        @Autowired
        public SchemaInitBean(DataSource dataSource,
                              @Value("${db.schema.entities}") String entitiesSchemaName,
                              @Value("${db.schema.service}") String serviceSchemaName,
                              @Value("${db.schema.audit}") String auditSchemaName) {
            this.dataSource = dataSource;
            this.entitiesSchemaName = entitiesSchemaName;
            this.serviceSchemaName = serviceSchemaName;
            this.auditSchemaName = auditSchemaName;
        }

        @Override
        public void afterPropertiesSet() {
            try (Connection conn = dataSource.getConnection();
                 Statement statement = conn.createStatement()) {
                String query = CREATE_SCHEMA_QUERY +
                        CREATE_SCHEMA_QUERY +
                        CREATE_SCHEMA_QUERY;
                statement.execute(String.format(query, entitiesSchemaName, serviceSchemaName, auditSchemaName));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create schema", e);
            }
        }
    }


    @ConditionalOnBean(SchemaInitBean.class)
    static class SpringLiquibaseDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {

        SpringLiquibaseDependsOnPostProcessor() {
            // Configure the 3rd party SpringLiquibase bean to depend on our SchemaInitBean
            super(SpringLiquibase.class, SchemaInitBean.class);
        }
    }
}

