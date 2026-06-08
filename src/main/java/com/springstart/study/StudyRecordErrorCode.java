package com.springstart.study;

import org.springframework.http.HttpStatus;

public enum StudyRecordErrorCode {
    INVALID_TITLE(HttpStatus.BAD_REQUEST,"Title cannot be null or empty"),
    INVALID_STUDY_MINUTES(HttpStatus.BAD_REQUEST,"StudyMinutes cannot be less than 1"),
    STUDY_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND,"StudyRecord not found"),
    STUDY_RECORD_ALREADY_COMPLETED(HttpStatus.CONFLICT,"This record is already completed"),;

    private final HttpStatus httpStatus;
    private String message;

    StudyRecordErrorCode(HttpStatus httpStatus, String message)
    {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public String getMessage() {
        return message;
    }
}
