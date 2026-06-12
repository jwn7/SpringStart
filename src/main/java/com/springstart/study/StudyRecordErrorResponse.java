package com.springstart.study;

public class StudyRecordErrorResponse {

    private Integer status;
    private String errorCode;
    private String message;

    public StudyRecordErrorResponse(StudyRecordErrorCode errorCode) {
        this.status = errorCode.getHttpStatus().value();
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }


    public Integer getStatus() {

        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getMessage() {

        return message;
    }
}
