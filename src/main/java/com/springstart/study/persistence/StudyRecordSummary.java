package com.springstart.study.persistence;

public class StudyRecordSummary {

    private final Long id;
    private final String title;
    private final int studyMinutes;

    public int getStudyMinutes() {
        return studyMinutes;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public StudyRecordSummary(Long id, String title, int studyMinutes) {
        this.id = id;
        this.title = title;
        this.studyMinutes = studyMinutes;
    }
}

