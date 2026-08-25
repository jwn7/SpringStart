package com.springstart.study.persistence;

public record StudyRecordJpaResponse(
        Long id,
        String title,
        String content,
        int studyMinutes,
        boolean completed
) {
    public StudyRecordJpaResponse(StudyRecordEntity entity) {
        this(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getStudyMinutes(),
                entity.isCompleted()
        );
    }
}
