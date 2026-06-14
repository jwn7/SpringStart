package com.springstart.study;

public class StudyRecordErrorResponse {

    private final Integer status;
    private final String errorCode;
    private final String message;

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
