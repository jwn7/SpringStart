package com.springstart.study.service;

import com.springstart.study.exception.StudyRecordErrorCode;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.persistence.StudyRecordEntity;
import com.springstart.study.persistence.StudyRecordJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudyRecordJpaService {

    private final StudyRecordJpaRepository studyRecordJpaRepository;

    public StudyRecordJpaService(StudyRecordJpaRepository studyRecordJpaRepository) {
        this.studyRecordJpaRepository = studyRecordJpaRepository;
    }

    @Transactional
    public void changeStudyMinutes(Long id, int studyMinutes) {
        StudyRecordEntity record = studyRecordJpaRepository.findById(id).orElseThrow(() -> new StudyRecordNotFoundException(StudyRecordErrorCode.STUDY_RECORD_NOT_FOUND));

        record.changeStudyMinutes(studyMinutes);


    }
}
