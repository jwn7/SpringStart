package com.springstart.study.service;

import com.springstart.study.exception.StudyRecordErrorCode;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.persistence.StudyRecordCursorResponse;
import com.springstart.study.persistence.StudyRecordEntity;
import com.springstart.study.persistence.StudyRecordJpaRepository;
import com.springstart.study.persistence.StudyRecordJpaResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public StudyRecordCursorResponse findNextByCursor(Long cursorId, int size) {

        Pageable pageable = PageRequest.of(0, size + 1);

        List<StudyRecordEntity> list = studyRecordJpaRepository.findNextByCursor(cursorId, pageable);

        boolean hasNext = list.size() > size;

        int contentSize = Math.min(list.size(), size);

        List<StudyRecordEntity> content = list.subList(0, contentSize);

        Long nextCursor = hasNext ? content.get(content.size() - 1).getId() : null;
        return new StudyRecordCursorResponse(content.stream().map(StudyRecordJpaResponse::new).toList(), nextCursor, hasNext);
    }
}
