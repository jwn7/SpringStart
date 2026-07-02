package com.springstart.study.service;

import com.springstart.study.config.AppConfig;
import com.springstart.study.exception.StudyRecordNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;


class StudyRecordTransactionTest {


    @Test
    public void transactionTest()
    {
        AnnotationConfigApplicationContext ac = getAc();

        StudyRecordTransactionPracticeService practiceService =
                ac.getBean(StudyRecordTransactionPracticeService.class);

        StudyRecordService studyRecordService =
                ac.getBean(StudyRecordService.class);
        Assertions.assertThrows(RuntimeException.class, () -> practiceService.createThenThrow(1L, "test", "test", 30));

        Assertions.assertThrows(StudyRecordNotFoundException.class, () -> studyRecordService.findById(1L));

        ac.close();
    }

    @Test
    public void transactionTest2()
    {
        AnnotationConfigApplicationContext ac = getAc();
        StudyRecordTransactionPracticeService practiceService =
                ac.getBean(StudyRecordTransactionPracticeService.class);

        StudyRecordService studyRecordService =
                ac.getBean(StudyRecordService.class);
        Assertions.assertThrows(Exception.class, () -> practiceService.createThenThrowChecked(1L, "test", "test", 30));

        Assertions.assertThrows(StudyRecordNotFoundException.class, () -> studyRecordService.findById(1L));

        ac.close();
    }

    private static AnnotationConfigApplicationContext getAc() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.getEnvironment().setActiveProfiles("jdbc");
        ac.register(AppConfig.class);
        ac.refresh();
        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
        jdbcTemplate.execute("DROP TABLE IF EXISTS study_records");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS study_records (" +
                "id BIGINT PRIMARY KEY," +
                "title varchar(255) NOT NULL," +
                "content varchar(255)," +
                "study_minutes INTEGER NOT NULL," +
                "completed BOOLEAN NOT NULL DEFAULT FALSE )");
        return ac;
    }

    @Test
    public void propagationTest() {
        AnnotationConfigApplicationContext ac = getAc();
        StudyRecordTransactionPracticeService practiceService =
                ac.getBean(StudyRecordTransactionPracticeService.class);

        StudyRecordService studyRecordService =
                ac.getBean(StudyRecordService.class);
        Assertions.assertThrows(RuntimeException.class, () -> practiceService.outerMethod(1L, "test", "test", 30, 2L));

        Assertions.assertThrows(StudyRecordNotFoundException.class, () -> studyRecordService.findById(1L));
        Assertions.assertThrows(StudyRecordNotFoundException.class, () -> studyRecordService.findById(2L));

        ac.close();
    }

    @Test
    public void propagationTest2() {
        AnnotationConfigApplicationContext ac = getAc();
        StudyRecordTransactionPracticeService practiceService =
                ac.getBean(StudyRecordTransactionPracticeService.class);

        StudyRecordService studyRecordService =
                ac.getBean(StudyRecordService.class);

        Assertions.assertThrows(RuntimeException.class, () -> practiceService.outerWithoutTransactionThenThrow(1L, "test", "test", 30, 2L));

        Assertions.assertNotNull(studyRecordService.findById(1L));
        Assertions.assertNotNull(studyRecordService.findById(2L));
        ac.close();
    }

}