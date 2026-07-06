package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;

import java.util.List;

public interface StudyRecordMapper {

    StudyRecord findById(Long id);

    List<StudyRecord> findAll();

    List<StudyRecord> findByMinStudyMinutes(int minStudyMinutes);
}
