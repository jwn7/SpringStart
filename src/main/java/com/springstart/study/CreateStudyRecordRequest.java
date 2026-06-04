package com.springstart.study;

public class CreateStudyRecordRequest {


    private Long id;
    private String title;
    private String content;
    private int studyMinutes;

    public CreateStudyRecordRequest() {
    }
    public Long  getId() {
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

}
