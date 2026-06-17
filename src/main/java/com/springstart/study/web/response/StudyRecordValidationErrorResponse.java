package com.springstart.study.web.response;


import com.springstart.study.exception.StudyRecordErrorCode;

import java.util.List;

public class StudyRecordValidationErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;
    private final List<StudyRecordFieldErrorResponse> errors;


    public StudyRecordValidationErrorResponse(StudyRecordErrorCode studyRecordErrorCode, List<StudyRecordFieldErrorResponse> errors) {

        this.status = studyRecordErrorCode.getHttpStatus().value();
        this.errorCode = studyRecordErrorCode.name();
        this.message = studyRecordErrorCode.getMessage();
        this.errors = errors;
    }
    public int getStatus() {
        return status;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public String getMessage() {
        return message;
    }
    public List<StudyRecordFieldErrorResponse> getErrors() {
        return errors;
    }
}
