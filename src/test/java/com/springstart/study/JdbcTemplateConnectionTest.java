package com.springstart.study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class JdbcTemplateConnectionTest {

    @Test
    void jdbcTemplateConnectionTest() {
        // 1. DataSource 생성
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // 2. JdbcTemplate 생성
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:study_record_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // 3. CREATE TABLE 실행
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS study_records (" +
                "id BIGINT PRIMARY KEY," +
                "title varchar(255) NOT NULL," +
                "content varchar(255)," +
                "study_minutes INTEGER NOT NULL," +
                "completed BOOLEAN NOT NULL DEFAULT FALSE )");
        // 4. INSERT 실행
        int row = jdbcTemplate.update("INSERT INTO study_records (id, title, content, study_minutes, completed) VALUES (?, ?, ?, ?, ?)",
                1L,
                "JDBC",
                "connection test",
                30,
                false);
        Assertions.assertEquals(1, row);

        // 5. SELECT COUNT(*) 조회
        Integer count = jdbcTemplate.queryForObject("select count(*) from study_records", Integer.class);
        // 6. Assertions.assertEquals(1, count)
        Assertions.assertEquals(1, count);
    }
}
