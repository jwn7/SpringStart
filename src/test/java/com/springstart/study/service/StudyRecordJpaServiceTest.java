package com.springstart.study.service;

import com.springstart.study.exception.StudyRecordErrorCode;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.persistence.StudyRecordEntity;
import com.springstart.study.persistence.StudyRecordJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(StudyRecordJpaService.class)
class StudyRecordJpaServiceTest {

    @Autowired
    private StudyRecordJpaService studyRecordJpaService;

    @Autowired
    private StudyRecordJpaRepository studyRecordJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    void changeStudyMinutesTest() {

        StudyRecordEntity studyRecord = new StudyRecordEntity("3", "3", 60);
        studyRecordJpaRepository.save(studyRecord);
        studyRecordJpaService.changeStudyMinutes(studyRecord.getId(), 30);

        em.flush();
        em.clear();

        StudyRecordEntity studyRecordEntity = studyRecordJpaRepository.findById(studyRecord.getId()).orElseThrow(() -> new StudyRecordNotFoundException(StudyRecordErrorCode.STUDY_RECORD_NOT_FOUND));
        Assertions.assertEquals(30, studyRecordEntity.getStudyMinutes());
    }

    @Test
    void notFoundIdExceptionTest() {

        StudyRecordNotFoundException studyRecordNotFoundException = Assertions.assertThrows(StudyRecordNotFoundException.class,
                () -> studyRecordJpaService.changeStudyMinutes(12L, 123));

        Assertions.assertEquals(StudyRecordErrorCode.STUDY_RECORD_NOT_FOUND, studyRecordNotFoundException.getErrorCode());


    }

}