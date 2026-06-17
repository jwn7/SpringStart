package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;

import java.util.List;
import java.util.Optional;

public interface StudyRecordRepository {

    StudyRecord save(StudyRecord studyRecord);

    Optional<StudyRecord> findById(Long id);

    List<StudyRecord> findAll();

    void deleteById(Long id);

    void clear();
}
