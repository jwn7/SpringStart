package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudyRecordMapper {

    StudyRecord findById(Long id);

    List<StudyRecord> findAll();

    List<StudyRecord> findByMinStudyMinutes(int minStudyMinutes);

    List<StudyRecord> findByTitleAndMinStudyMinutes(
            @Param("title") String title,
            @Param("minStudyMinutes") int minStudyMinutes
    );
}
