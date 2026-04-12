//package com.sunghyun.batch.config;
//
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class BatchConfig {
//
//    @Bean
//    public PlatformTransactionManager transactionManager(DataSource dataSource){
//        return new DataSourceTransactionManager(dataSource);
//    }
//}
