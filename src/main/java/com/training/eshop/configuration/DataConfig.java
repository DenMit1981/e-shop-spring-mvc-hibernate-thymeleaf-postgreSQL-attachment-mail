package com.training.eshop.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(ignoreResourceNotFound = true, value = "classpath:db/hibernate.properties")
@ComponentScan(basePackages = "com.training.eshop")
public class DataConfig implements WebMvcConfigurer {

    public static final String ENTITY_PATH = "com.training.eshop.model";

    @Value("${hibernate.hbm2ddl.auto}")
    private String hbm2ddlAuto;

    @Value("${hibernate.hbm2ddl.import_files}")
    private String hbm2ddlImport;

    @Value("${hibernate.dialect}")
    private String dialect;

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.URL}")
    private String dbUrl;

    @Value("${db.userName}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Bean
    public DriverManagerDataSource getDataSource() {
        DriverManagerDataSource bds = new DriverManagerDataSource();

        bds.setDriverClassName(dbDriver);
        bds.setUrl(dbUrl);
        bds.setUsername(dbUsername);
        bds.setPassword(dbPassword);

        return bds;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();

        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
        hibernateProperties.setProperty("hibernate.hbm2ddl.import_files", hbm2ddlImport);
        hibernateProperties.setProperty("hibernate.dialect", dialect);

        return hibernateProperties;
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(getDataSource());
        factoryBean.setPackagesToScan(ENTITY_PATH);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());

        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();

        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
