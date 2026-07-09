package com.springstart.study.repository;

public class StudyRecordSearchCondition {

    private String title;

    private Integer minStudyMinutes;

    public StudyRecordSearchCondition(String title, Integer minStudyMinutes) {
        this.title = title;
        this.minStudyMinutes = minStudyMinutes;
    }
    public String getTitle() {
        return title;
    }
    public Integer getMinStudyMinutes() {
        return minStudyMinutes;
    }
}
