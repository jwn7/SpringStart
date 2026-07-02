package com.springstart.study.service;


import com.springstart.study.domain.StudyRecord;
import com.springstart.study.exception.StudyRecordErrorCode;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StudyRecordService {

    private final StudyRecordRepository repository;

    public StudyRecordService(StudyRecordRepository repository) {
        this.repository = repository;
    }

    public StudyRecord create(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);
        return record;
    }

    public StudyRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudyRecordNotFoundException(StudyRecordErrorCode.STUDY_RECORD_NOT_FOUND));

    }

    public List<StudyRecord> findAll() {
        return repository.findAll();
    }

    public StudyRecord update(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = findById(id);
        record.update(title, content, studyMinutes);
        return record;
    }

    public StudyRecord complete(Long id) {
        StudyRecord record = findById(id);
        record.complete();
        return record;
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }

    @Transactional
    public void createThenThrow(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);

        throw new RuntimeException("rollback test");
    }
}
