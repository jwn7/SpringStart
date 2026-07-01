package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;
import java.util.Optional;


class JdbcStudyRecordRepositoryTest {

    private JdbcStudyRecordRepository jdbcStudyRecordRepository;
    @BeforeEach
    void jdbcTemplateConnectionTest() {
        // 1. DataSource 생성
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // 2. JdbcTemplate 생성
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:study_record_test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DROP TABLE IF EXISTS study_records");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS study_records (" +
                "id BIGINT PRIMARY KEY," +
                "title varchar(255) NOT NULL," +
                "content varchar(255)," +
                "study_minutes INTEGER NOT NULL," +
                "completed BOOLEAN NOT NULL DEFAULT FALSE )");

        jdbcStudyRecordRepository = new JdbcStudyRecordRepository(jdbcTemplate);
    }

    @Test
    void saveAndFindByIdTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L,"test","test",30);
        jdbcStudyRecordRepository.save(studyRecord);
        Optional<StudyRecord> findById = jdbcStudyRecordRepository.findById(studyRecord.getId());


        Assertions.assertTrue(findById.isPresent());

        StudyRecord find = findById.get();
        Assertions.assertEquals(1L,find.getId());
        Assertions.assertEquals("test",find.getTitle());
        Assertions.assertEquals("test",find.getContent());
        Assertions.assertEquals(30,find.getStudyMinutes());
        Assertions.assertFalse(find.isCompleted());
    }

    @Test
    void findByIdNotFoundTest()
    {
        Optional<StudyRecord> id = jdbcStudyRecordRepository.findById(1L);
        Assertions.assertFalse(id.isPresent());

    }

    @Test
    void completeTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L,"test","test",30);
        studyRecord.complete();

        jdbcStudyRecordRepository.save(studyRecord);
        Optional<StudyRecord> findById = jdbcStudyRecordRepository.findById(1L);
        Assertions.assertTrue(findById.isPresent());
        StudyRecord find = findById.get();
        Assertions.assertTrue(find.isCompleted());
    }

    @Test
    void findAllTest(){
        StudyRecord studyRecord = new StudyRecord(1L,"test","test",30);
        StudyRecord studyRecord2 = new StudyRecord(2L,"test2","test2",30);

        jdbcStudyRecordRepository.save(studyRecord);
        jdbcStudyRecordRepository.save(studyRecord2);

        List<StudyRecord> list = jdbcStudyRecordRepository.findAll();
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals(1L, list.get(0).getId());
        Assertions.assertEquals("test", list.get(0).getTitle());
        Assertions.assertEquals(2L, list.get(1).getId());
        Assertions.assertEquals("test2", list.get(1).getTitle());
    }

    @Test
    void deleteByIdTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L,"test","test",30);
        jdbcStudyRecordRepository.save(studyRecord);
        jdbcStudyRecordRepository.deleteById(studyRecord.getId());

        Optional<StudyRecord> findById = jdbcStudyRecordRepository.findById(1L);
        Assertions.assertFalse(findById.isPresent());
    }

    @Test
    void clearTest()
    {
        StudyRecord sr = new StudyRecord(1L,"test","test",30);
        StudyRecord r = new  StudyRecord(2L,"test2","test2",30);

        jdbcStudyRecordRepository.save(sr);
        jdbcStudyRecordRepository.save(r);

        jdbcStudyRecordRepository.clear();

        Assertions.assertTrue(jdbcStudyRecordRepository.findAll().isEmpty());
        Assertions.assertEquals(0, jdbcStudyRecordRepository.findAll().size());
    }
}

