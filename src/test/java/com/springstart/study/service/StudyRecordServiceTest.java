package com.springstart.study.service;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.repository.InMemoryStudyRecordRepository;
import com.springstart.study.repository.StudyRecordRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class StudyRecordServiceTest {

    private StudyRecordService service;
    @BeforeEach
    void setUp() {
        StudyRecordRepository repository = new InMemoryStudyRecordRepository();
        service = new StudyRecordService(repository );
    }
    @Test
    void createAndFindByIdTest()
    {
        StudyRecord record = service.create(1L,"Java Service", "service test", 40);
        Assertions.assertEquals(1L,record.getId());
        Assertions.assertEquals("Java Service",record.getTitle());
        Assertions.assertEquals(40,record.getStudyMinutes());
    }
    @Test
    void deleteTest()
    {
        StudyRecord record = service.create(1L, "Java Service", "service test", 40);
        service.delete(record.getId());

        Assertions.assertThrows(StudyRecordNotFoundException.class, () -> service.findById(record.getId()));
    }

    @Test
    void updateTest()
    {
        StudyRecord record = service.create(1L,"Java Service", "service test", 40);
        StudyRecord update = service.update(record.getId(), "Spring test", "updated test", 50);
        Assertions.assertEquals(50,update.getStudyMinutes());
        Assertions.assertEquals("Spring test",update.getTitle());
        Assertions.assertEquals("updated test",update.getContent());
    }
    @Test
    void completeTest()
    {
        StudyRecord record = service.create(1L,"Java Service", "service test", 40);
        service.complete(record.getId());
        Assertions.assertTrue(service.findById(record.getId()).isCompleted());

    }
    @Test
    void findAllTest()
    {
        service.create(1L,"Java Service", "service test", 40);
        service.create(2L,"Spring test", "findAll test", 50);
        List<StudyRecord> records = service.findAll();
        Assertions.assertEquals(2,records.size());
        Assertions.assertEquals(1L,records.get(0).getId());
        Assertions.assertEquals(2L,records.get(1).getId());
    }
}
