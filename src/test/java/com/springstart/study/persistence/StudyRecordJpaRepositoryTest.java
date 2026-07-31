package com.springstart.study.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class StudyRecordJpaRepositoryTest {

    @Autowired
    private StudyRecordJpaRepository studyRecordJpaRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void save() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        assertNull(studyRecordEntity.getId());
        StudyRecordEntity save = studyRecordJpaRepository.save(studyRecordEntity);
        assertNotNull(studyRecordEntity.getId());
        assertEquals(save.getId(), studyRecordEntity.getId());
        assertSame(save, studyRecordEntity);
    }

    @Test
    void merge() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        StudyRecordEntity save = studyRecordJpaRepository.save(studyRecordEntity);

        assertTrue(entityManager.contains(save));

        entityManager.flush();

        entityManager.detach(save);

        assertFalse(entityManager.contains(save));

        StudyRecordEntity merged = studyRecordJpaRepository.save(save);

        assertNotSame(save, merged);
        assertEquals(save.getId(), merged.getId());

        assertTrue(entityManager.contains(merged));
        assertFalse(entityManager.contains(save));
    }

    @Test
    void mergeCopies() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        StudyRecordEntity save = studyRecordJpaRepository.save(studyRecordEntity);
        entityManager.flush();
        entityManager.detach(save);

        save.changeStudyMinutes(60);
        StudyRecordEntity merged = studyRecordJpaRepository.save(save);
        assertEquals(60, merged.getStudyMinutes());
        entityManager.flush();
        entityManager.clear();

        StudyRecordEntity te = studyRecordJpaRepository.findById(merged.getId()).orElseThrow();
        assertEquals(60, te.getStudyMinutes());
    }

    @Test
    void dirtyChecking() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        StudyRecordEntity save = studyRecordJpaRepository.save(studyRecordEntity);

        assertTrue(entityManager.contains(save));

        save.changeStudyMinutes(60);

        entityManager.flush();
        entityManager.clear();

        StudyRecordEntity record = studyRecordJpaRepository.findById(save.getId()).orElseThrow();
        assertEquals(60, record.getStudyMinutes());
    }


}
