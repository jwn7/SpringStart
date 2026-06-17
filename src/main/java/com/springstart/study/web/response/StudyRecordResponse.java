package com.springstart.study.web.response;

import com.springstart.study.domain.StudyRecord;

public class StudyRecordResponse {

    private Long id;
    private String title;
    private String content;
    private int studyMinutes;
    private boolean completed;

    public StudyRecordResponse(StudyRecord studyRecord) {
        this.id = studyRecord.getId();
        this.title = studyRecord.getTitle();
        this.content = studyRecord.getContent();
        this.studyMinutes = studyRecord.getStudyMinutes();
        this.completed = studyRecord.isCompleted();
    }

    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public int getStudyMinutes() {
        return studyMinutes;
    }
    public boolean isCompleted() {
        return completed;
    }
}
