package com.springstart.study.web.response;


import com.springstart.study.exception.StudyRecordErrorCode;

public class StudyRecordFieldErrorResponse {

    private String field;
    private String errorCode;
    private String message;

    public StudyRecordFieldErrorResponse(String fieldError, StudyRecordErrorCode studyRecordErrorCode)
    {
        this.field = fieldError;
        this.errorCode = studyRecordErrorCode.name();
        this.message = studyRecordErrorCode.getMessage();
    }

    public String getField() {
        return field;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public String getMessage() {
        return message;
    }
}
