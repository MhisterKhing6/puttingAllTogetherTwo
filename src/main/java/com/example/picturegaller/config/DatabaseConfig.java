package com.example.picturegaller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

import com.example.picturegaller.utils.ParameterStoreUtil;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {

    private final ParameterStoreUtil parameterstoreUtil;

    public DatabaseConfig(ParameterStoreUtil parameterstoreUtil) {
        this.parameterstoreUtil = parameterstoreUtil;
    };
    

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        String url = "jdbc:mysql://" + parameterstoreUtil.getParameter("/weekfiveproject/db/host", false) + ":"  + parameterstoreUtil.getParameter("/weekfiveproject/db/port", false) +  "/" + parameterstoreUtil.getParameter("/weekfiveproject/db/name", false);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + url);
        dataSource.setJdbcUrl(url); //parameterstoreUtil.getParameter("/myapp/db/url", false));
        dataSource.setUsername(parameterstoreUtil.getParameter("/weekfiveproject/db/username", false));
        dataSource.setPassword(parameterstoreUtil.getParameter("/weekfiveproject/db/password", true));
        return dataSource;
    }
}
