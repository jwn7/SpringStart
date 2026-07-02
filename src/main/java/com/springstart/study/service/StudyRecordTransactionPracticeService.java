package com.springstart.study.service;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyRecordTransactionPracticeService {

    private final StudyRecordRepository repository;
    private final StudyRecordInnerService innerService;
    public StudyRecordTransactionPracticeService(StudyRecordRepository studyRecordRepository,  StudyRecordInnerService studyRecordInnerService) {

        this.repository = studyRecordRepository;
        this.innerService = studyRecordInnerService;
    }
    @Transactional
    public void createThenThrow(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);

        throw new RuntimeException("rollback test");
    }

    @Transactional(rollbackFor = Exception.class)
    public void createThenThrowChecked(Long id, String title, String content, int studyMinutes) throws Exception {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);

        throw new Exception("rollback test");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void outerMethod(Long id, String title, String content, int studyMinutes,Long id2) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);
        innerService.innerMethod(id2, title, content, studyMinutes);
        throw new RuntimeException("rollback test");

    }

    public void outerWithoutTransactionThenThrow(Long id, String title, String content, int studyMinutes,Long id2) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);
        innerService.innerMethod(id2, title, content, studyMinutes);
        throw new RuntimeException("rollback test");
    }
}
