package com.springstart.study.exception;

public class StudyRecordAlreadyCompletedException extends RuntimeException {

    private final StudyRecordErrorCode errorCode;


    public StudyRecordAlreadyCompletedException(StudyRecordErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public StudyRecordErrorCode getErrorCode() {
        return errorCode;
    }
}
