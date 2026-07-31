package com.springstart.study.persistence;


import jakarta.persistence.*;

@Entity
@Table(name = "study_records")
public class StudyRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String title;

    private String content;

    @Column(name = "study_minutes")
    private int studyMinutes;

    private boolean completed;

    protected StudyRecordEntity() {
    }

    public StudyRecordEntity(String title, String content, int studyMinutes) {
        this.title = title;
        this.content = content;
        this.studyMinutes = studyMinutes;
        this.completed = false;
    }

    public void changeStudyMinutes(int studyMinutes) {
        this.studyMinutes = studyMinutes;
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

    public Long getId() {
        return id;
    }
}
