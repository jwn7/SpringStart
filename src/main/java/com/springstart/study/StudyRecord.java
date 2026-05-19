package com.springstart.study;

public class StudyRecord {
    private Long id;
    private String title;
    private String content;
    private int studyMinutes;
    private boolean completed;

    public StudyRecord(Long id, String title, String content, int studyMinutes) {
        validateTitle(title);
        validateStudyMinutes(studyMinutes);

        this.id = id;
        this.title = title;
        this.content = content;
        this.studyMinutes = studyMinutes;
        this.completed = false;

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

    public void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }

    public void validateStudyMinutes(int studyMinutes) {
        if (studyMinutes < 1) {
            throw new IllegalArgumentException("StudyMinutes cannot be less than 1");
        }
    }

    public void complete() {
        this.completed = true;
    }

    public void update(String title, String content, int studyMinutes) {
        if (this.completed)
            throw new IllegalStateException("This record is already completed");
        validateTitle(title);
        validateStudyMinutes(studyMinutes);
        this.title = title;
        this.content = content;
        this.studyMinutes = studyMinutes;
    }

}

