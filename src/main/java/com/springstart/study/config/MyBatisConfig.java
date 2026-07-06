package com.springstart.study.config;


import com.springstart.study.repository.StudyRecordMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@Profile("jdbc")
public class MyBatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));
        return  sqlSessionFactoryBean.getObject();
    }

    @Bean
    public MapperFactoryBean<StudyRecordMapper> studyRecordMapper(SqlSessionFactory sqlSessionFactory) {
        MapperFactoryBean<StudyRecordMapper> mapperFactoryBean = new MapperFactoryBean<>(StudyRecordMapper.class);
        mapperFactoryBean.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactoryBean;
    }
}
