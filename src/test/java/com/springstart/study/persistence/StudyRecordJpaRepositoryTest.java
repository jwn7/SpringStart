package com.springstart.study.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.*;

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

        assertEquals(90, list.get(0).studyMinutes());
        assertEquals(60, list.get(1).studyMinutes());
        assertEquals("title1", list.get(0).title());

    }

    @Test
    void findAllWithPageableTest() {
        initEntity();

        Pageable pageable = PageRequest.of(
                0,
                2,
                Sort.by("studyMinutes").descending()
        );

        Page<StudyRecordEntity> page = studyRecordJpaRepository.findAll(pageable);

        assertEquals(2, page.getContent().size());
        assertEquals(3, page.getTotalElements());
        assertEquals(2, page.getTotalPages());

        assertEquals(90, page.getContent().get(0).getStudyMinutes());
        assertEquals(60, page.getContent().get(1).getStudyMinutes());

        assertEquals(0, page.getNumber());
        assertTrue(page.hasNext());


    }

    @Test
    void findWithSliceTest() {
        initEntity();


        Pageable pageable = PageRequest.of(
                0,
                2,
                Sort.by("studyMinutes").descending()
        );

        Slice<StudyRecordEntity> slice = studyRecordJpaRepository.findByCompleted(false, pageable);

        assertEquals(2, slice.getContent().size());
        assertEquals(90, slice.getContent().get(0).getStudyMinutes());
        assertEquals(60, slice.getContent().get(1).getStudyMinutes());
        assertEquals(0, slice.getNumber());
        assertTrue(slice.hasNext());
    }

    private void initEntity() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);
        StudyRecordEntity studyRecordEntity1 = new StudyRecordEntity("title1", "t", 60);
        studyRecordJpaRepository.save(studyRecordEntity1);
        StudyRecordEntity studyRecordEntity2 = new StudyRecordEntity("title2", "t", 90);
        studyRecordJpaRepository.save(studyRecordEntity2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findWithCursorTest() {
        initEntity();

        Pageable pageable = PageRequest.of(0, 2);
        List<StudyRecordEntity> list = studyRecordJpaRepository.findNextByCursor(null, pageable);

        Long cursorId = list.get(1).getId();

        List<StudyRecordEntity> nextList = studyRecordJpaRepository.findNextByCursor(cursorId, pageable);

        assertEquals(2, list.size());
        assertEquals(90, list.get(0).getStudyMinutes());
        assertEquals(60, list.get(1).getStudyMinutes());

        assertEquals(30, nextList.get(0).getStudyMinutes());
        assertEquals(1, nextList.size());
        assertTrue(nextList.get(0).getId() < cursorId);
    }

    @Test
    void findNextByCursorTest() {
        initEntity();
        int size = 2;
        Pageable pageable = PageRequest.of(0, size + 1);

        List<StudyRecordEntity> fetched = studyRecordJpaRepository.findNextByCursor(null, pageable);
        boolean hasNext = fetched.size() > size;
        assertEquals(3, fetched.size());

        assertTrue(hasNext);

        List<StudyRecordEntity> content = fetched.subList(0, size);
        List<StudyRecordJpaResponse> list = content.stream()
                .map(StudyRecordJpaResponse::new)
                .toList();
        Long nextCursor = content.get(1).getId();
        StudyRecordCursorResponse cursorResponse = new StudyRecordCursorResponse(list, nextCursor, hasNext);

        assertEquals(2, cursorResponse.contents().size());
        assertEquals(90, cursorResponse.contents().get(0).studyMinutes());
        assertEquals(nextCursor, cursorResponse.nextCursor());
        assertTrue(cursorResponse.hasNext());

        List<StudyRecordEntity> nextList = studyRecordJpaRepository.findNextByCursor(nextCursor, pageable);

        assertEquals(2, content.size());
        assertEquals(1, nextList.size());

        assertTrue(nextList.get(0).getId() < nextCursor);


    }
}
