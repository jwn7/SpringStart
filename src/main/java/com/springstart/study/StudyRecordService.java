package com.springstart.study;

import java.util.List;

public class StudyRecordService {

    private final InMemoryStudyRecordRepository repository;

    public StudyRecordService(InMemoryStudyRecordRepository repository) {
        this.repository = repository;
    }

    public StudyRecord create(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);
        return record;
    }

    public StudyRecord findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("StudyRecord not found"));

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
