package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;
import org.apache.ibatis.annotations.Select;

public interface StudyRecordMapper {


    @Select("SELECT id, title, content, study_minutes FROM study_records WHERE id = #{id}")
    StudyRecord findById(Long id);
}
