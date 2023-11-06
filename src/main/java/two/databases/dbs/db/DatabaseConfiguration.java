package two.databases.dbs.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "two.databases.dbs.user", // Adjust this to your packages
        entityManagerFactoryRef = "multiEntityManager",
        transactionManagerRef = "multiTransactionManager"
)
public class DatabaseConfiguration {

    @Primary
    @Bean(name = "apvDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.apv")
    public DataSource apvDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pgodinaDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.pgodina")
    public DataSource pgodinaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dataSource")
    public DataSource dataSource(
            @Qualifier("apvDataSource") DataSource apvDataSource,
            @Qualifier("pgodinaDataSource") DataSource pgodinaDataSource) {

        AbstractRoutingDataSource routingDataSource = new MultiRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(ClientNames.APV, apvDataSource);
        targetDataSources.put(ClientNames.PGODINA, pgodinaDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(apvDataSource); // Default data source
        return routingDataSource;
    }

    @Primary
    @Bean(name = "multiEntityManager")
    public LocalContainerEntityManagerFactoryBean multiEntityManager(
            @Qualifier("dataSource") DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[] { "two.databases.dbs.user" }); // Your JPA entities package
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(hibernateProperties());
        return em;
    }

    @Primary
    @Bean(name = "multiTransactionManager")
    public PlatformTransactionManager multiTransactionManager(
            @Qualifier("multiEntityManager") LocalContainerEntityManagerFactoryBean multiEntityManager) {

        return new JpaTransactionManager(multiEntityManager.getObject());
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Ensure this matches your DB dialect
        return properties;
    }
}


//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.orm.jpa.JpaTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "multiEntityManager",
//        transactionManagerRef = "multiTransactionManager",
//        basePackages = {"two.databases.dbs.user"} // Adjust this to your packages
//)
//public class DatabaseConfiguration {
//
//    @Primary
//    @Bean(name = "apvDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.apv")
//    public DataSource apvDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "pgodinaDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.pgodina")
//    public DataSource pgodinaDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = "multiEntityManager")
//    public LocalContainerEntityManagerFactoryBean multiEntityManager(
//            @Qualifier("apvDataSource") DataSource apvDataSource,
//            @Qualifier("pgodinaDataSource") DataSource pgodinaDataSource) {
//
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(apvDataSource); // Primary data source
//        em.setPackagesToScan("two.databases.dbs.user"); // Your JPA entities package
//
//        // Set the JPA vendor adapter
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//
//        // If you have additional properties, consider setting them here
//        em.setJpaProperties(hibernateProperties());
//        return em;
//    }
//
//    @Primary
//    @Bean(name = "multiTransactionManager")
//    public PlatformTransactionManager multiTransactionManager(
//            @Qualifier("multiEntityManager") LocalContainerEntityManagerFactoryBean multiEntityManager) {
//
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(multiEntityManager.getObject());
//        return transactionManager;
//    }
//
//    private Properties hibernateProperties() {
//        Properties properties = new Properties();
//        properties.put("hibernate.hbm2ddl.auto", "update");
//        properties.put("hibernate.show_sql", "true");
//        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect"); // Ensure this matches your database dialect
//        return properties;
//    }
//
//    @Bean(name = "dataSource")
//    public DataSource dataSource(@Qualifier("apvDataSource") DataSource apvDataSource,
//                                 @Qualifier("pgodinaDataSource") DataSource pgodinaDataSource) {
//        MultiRoutingDataSource routingDataSource = new MultiRoutingDataSource();
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(ClientNames.APV, apvDataSource);
//        dataSourceMap.put(ClientNames.PGODINA, pgodinaDataSource);
//        routingDataSource.setTargetDataSources(dataSourceMap);
//        routingDataSource.setDefaultTargetDataSource(apvDataSource); // Set default data source here
//        return routingDataSource;
//    }
//
//}
