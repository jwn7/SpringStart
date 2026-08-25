package com.springstart.study.service;

import com.springstart.study.exception.StudyRecordErrorCode;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.persistence.StudyRecordCursorResponse;
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

    @Test
    void findNextByCursorReturnsFirstCursorResponse() {
        StudyRecordEntity sixtyMinutesRecord = initEntity();

        StudyRecordCursorResponse response = studyRecordJpaService.findNextByCursor(null, 2);

        Assertions.assertEquals(2, response.contents().size());
        Assertions.assertEquals(90, response.contents().get(0).studyMinutes());
        Assertions.assertEquals(60, response.contents().get(1).studyMinutes());
        Assertions.assertTrue(response.hasNext());
        Assertions.assertEquals(sixtyMinutesRecord.getId(), response.nextCursor());

    }

    @Test
    void findNextByCursorReturnsLastCursorResponse() {
        initEntity();

        StudyRecordCursorResponse firstResponse = studyRecordJpaService.findNextByCursor(null, 2);
        StudyRecordCursorResponse secondResponse = studyRecordJpaService.findNextByCursor(firstResponse.nextCursor(), 2);

        Assertions.assertEquals(1, secondResponse.contents().size());
        Assertions.assertEquals(30, secondResponse.contents().get(0).studyMinutes());
        Assertions.assertFalse(secondResponse.hasNext());
        Assertions.assertNull(secondResponse.nextCursor());
    }

    private StudyRecordEntity initEntity() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);
        StudyRecordEntity studyRecordEntity1 = new StudyRecordEntity("title1", "t", 60);
        studyRecordJpaRepository.save(studyRecordEntity1);
        StudyRecordEntity studyRecordEntity2 = new StudyRecordEntity("title2", "t", 90);
        studyRecordJpaRepository.save(studyRecordEntity2);

        em.flush();
        em.clear();

        return studyRecordEntity1;
    }

}