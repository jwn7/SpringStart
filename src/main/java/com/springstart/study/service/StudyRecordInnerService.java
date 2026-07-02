package com.springstart.study.service;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.repository.StudyRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyRecordInnerService {

    private final StudyRecordRepository repository;

    public StudyRecordInnerService(StudyRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void innerMethod(Long id, String title, String content, int studyMinutes) {
        StudyRecord record = new StudyRecord(id, title, content, studyMinutes);
        repository.save(record);

    }

}
