package com.springstart.study;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudyRecordService {

    private final StudyRecordRepository repository;

    public StudyRecordService(@Qualifier("inMemoryStudyRecordRepository") StudyRecordRepository repository) {
        this.repository = repository;
    }

    public StudyRecord create(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);
        return record;
    }

    public StudyRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new StudyRecordNotFoundException("StudyRecord not found"));

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

}
