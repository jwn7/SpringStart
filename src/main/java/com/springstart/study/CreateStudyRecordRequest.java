package com.springstart.study;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateStudyRecordRequest {


    private Long id;
    @NotBlank
    private String title;
    private String content;
    @Min(1)
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
