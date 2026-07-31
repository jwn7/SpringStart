package com.springstart.study.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

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


    @Test
    void findByTitleTest() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);

        StudyRecordEntity e1 = new StudyRecordEntity("title1", "t1", 30);
        studyRecordJpaRepository.save(e1);

        StudyRecordEntity e2 = new StudyRecordEntity("title1", "t1", 30);
        studyRecordJpaRepository.save(e2);

        entityManager.flush();
        entityManager.clear();

        List<StudyRecordEntity> list = studyRecordJpaRepository.findByTitle("title1");

        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(record -> record.getTitle().equals("title1")));
    }

    @Test
    void findByTitleAndStudyMinutesTest() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title1", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);

        StudyRecordEntity e1 = new StudyRecordEntity("title1", "t1", 60);
        studyRecordJpaRepository.save(e1);

        StudyRecordEntity e2 = new StudyRecordEntity("title1", "t1", 90);
        studyRecordJpaRepository.save(e2);

        entityManager.flush();
        entityManager.clear();

        List<StudyRecordEntity> list = studyRecordJpaRepository.findByTitleOrderByStudyMinutesDesc("title1");

        assertEquals(3, list.size());
        assertEquals(90, list.get(0).getStudyMinutes());
        assertEquals(60, list.get(1).getStudyMinutes());
        assertEquals(30, list.get(2).getStudyMinutes());

    }

    @Test
    void findFirstByTitleAndStudyMinutesTest() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title1", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);

        StudyRecordEntity e1 = new StudyRecordEntity("title1", "t1", 60);
        studyRecordJpaRepository.save(e1);

        StudyRecordEntity e2 = new StudyRecordEntity("title1", "t1", 90);
        studyRecordJpaRepository.save(e2);

        entityManager.flush();
        entityManager.clear();


        StudyRecordEntity e3 = studyRecordJpaRepository.findFirstByTitleOrderByStudyMinutesDesc("title1").orElseThrow();
        assertEquals(90, e3.getStudyMinutes());

        assertTrue(studyRecordJpaRepository.findFirstByTitleOrderByStudyMinutesDesc("123").isEmpty());
    }

    @Test
    void findByStudyMinutesGreaterThanTest() {
        studyRecordJpaRepository.save(new StudyRecordEntity("thirty", "t", 30));
        studyRecordJpaRepository.save(new StudyRecordEntity("thirty-one", "t", 31));
        studyRecordJpaRepository.save(new StudyRecordEntity("sixty", "t", 60));

        entityManager.flush();
        entityManager.clear();

        List<StudyRecordEntity> records = studyRecordJpaRepository.findByStudyMinutesGreaterThan(30);

        assertEquals(2, records.size());
        assertTrue(records.stream().allMatch(record -> record.getStudyMinutes() > 30));
        assertTrue(records.stream().noneMatch(record -> record.getStudyMinutes() == 30));
    }

    @Test
    void searchByTitleTest() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title1", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);
        StudyRecordEntity e1 = new StudyRecordEntity("title1", "t1", 60);
        studyRecordJpaRepository.save(e1);
        StudyRecordEntity e2 = new StudyRecordEntity("title1", "t1", 90);
        studyRecordJpaRepository.save(e2);
        StudyRecordEntity e3 = new StudyRecordEntity("t", "t1", 120);
        studyRecordJpaRepository.save(e3);

        entityManager.flush();
        entityManager.clear();


        List<StudyRecordEntity> list = studyRecordJpaRepository.searchByTitleAndMinimumStudyMinutes("title1", 30);
        assertEquals(2, list.size());
        assertTrue(list.stream().allMatch(record -> record.getTitle().equals("title1")));
        assertEquals(90, list.get(0).getStudyMinutes());
        assertEquals(60, list.get(1).getStudyMinutes());
    }

    @Test
    void dtoProjectionTest() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);
        StudyRecordEntity e1 = new StudyRecordEntity("title1", "t1", 60);
        studyRecordJpaRepository.save(e1);
        StudyRecordEntity e2 = new StudyRecordEntity("title1", "t1", 90);
        studyRecordJpaRepository.save(e2);

        entityManager.flush();
        entityManager.clear();

        List<StudyRecordSummary> list = studyRecordJpaRepository.findSummariesByTitle("title1");
        assertEquals(2, list.size());

        assertEquals(90, list.get(0).getStudyMinutes());
        assertEquals(60, list.get(1).getStudyMinutes());
        assertEquals("title1", list.get(0).getTitle());

    }
}
