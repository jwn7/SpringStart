package com.springstart.study;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UpdateStudyRecordRequest {

    @NotBlank
    private String title;
    private String content;
    @Min(1)
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
