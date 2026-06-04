package com.springstart.study;

public class UpdateStudyRecordRequest {


    private String title;
    private String content;
    private int studyMinutes;
    public UpdateStudyRecordRequest() {}


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
