package com.example.servingwebcontent;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@PropertySources({@PropertySource(value = "file://${APP_PATH}/config/application.properties", ignoreResourceNotFound = true) })
public class HibernateConfig {

	@Autowired
    private DataSource dataSource;

	@Primary
    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.example.servingwebcontent");
        return sessionFactory;
    }
    
}
