package com.springstart.study;

public class StudyRecordErrorResponse {

    private Integer status;
    private String message;

    public StudyRecordErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public StudyRecordErrorResponse(StudyRecordErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.message = errorCode.getMessage();
    }


    public Integer getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
