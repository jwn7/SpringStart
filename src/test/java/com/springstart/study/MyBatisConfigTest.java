package com.springstart.study;

import com.springstart.study.config.AppConfig;
import com.springstart.study.domain.StudyRecord;
import com.springstart.study.repository.StudyRecordMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MyBatisConfigTest {

    @Test
    public void studyRecordMapperBeanCreated()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles("jdbc");
        context.register(AppConfig.class);
        context.refresh();
        StudyRecordMapper studyRecordMapper = context.getBean(StudyRecordMapper.class);
        Assertions.assertNotNull(studyRecordMapper);

        context.close();
    }

    @Test
    public void findByIdTest()
    {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("jdbc");
        ac.register(AppConfig.class);   
        ac.refresh();

        StudyRecordMapper studyRecordMapper = ac.getBean(StudyRecordMapper.class);

        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
        jdbcTemplate.execute("DROP TABLE IF EXISTS study_records");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS study_records (" +
                "id BIGINT PRIMARY KEY," +
                "title varchar(255) NOT NULL," +
                "content varchar(255)," +
                "study_minutes INTEGER NOT NULL," +
                "completed BOOLEAN NOT NULL DEFAULT FALSE )");

        jdbcTemplate.update("INSERT INTO study_records (id, title, content, study_minutes, completed) VALUES (?,?,?,?,?)",
                1L,
                "test",
                "test",
                30,
                false);

        StudyRecord result = studyRecordMapper.findById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("test", result.getTitle());
        Assertions.assertEquals("test", result.getContent());
        Assertions.assertEquals(30, result.getStudyMinutes());
        ac.close();
    }

    @Test
    public void findAllTest()
    {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("jdbc");
        ac.register(AppConfig.class);
        ac.refresh();

        StudyRecordMapper studyRecordMapper = ac.getBean(StudyRecordMapper.class);

        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
        jdbcTemplate.execute("DROP TABLE IF EXISTS study_records");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS study_records (" +
                "id BIGINT PRIMARY KEY," +
                "title varchar(255) NOT NULL," +
                "content varchar(255)," +
                "study_minutes INTEGER NOT NULL," +
                "completed BOOLEAN NOT NULL DEFAULT FALSE )");

        jdbcTemplate.update("INSERT INTO study_records (id, title, content, study_minutes, completed) VALUES (?,?,?,?,?)",
                1L,
                "test",
                "test",
                30,
                false);

        jdbcTemplate.update("INSERT INTO study_records (id, title, content, study_minutes, completed) VALUES (?,?,?,?,?)",
                2L,
                "t",
                "t",
                50,
                false);

        List<StudyRecord> result = studyRecordMapper.findAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(2L, result.get(1).getId());

        ac.close();
    }
}
