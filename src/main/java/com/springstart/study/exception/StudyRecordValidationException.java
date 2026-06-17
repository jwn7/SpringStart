package com.springstart.study.exception;

public class StudyRecordValidationException extends RuntimeException {

    private final StudyRecordErrorCode errorCode;

    public StudyRecordValidationException(StudyRecordErrorCode errorCode)
    {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public StudyRecordErrorCode getErrorCode() {
        return errorCode;
    }
}
