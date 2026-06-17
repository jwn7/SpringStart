package com.springstart.study.exception;

public class StudyRecordNotFoundException extends RuntimeException {


    private final StudyRecordErrorCode errorCode;

    public StudyRecordNotFoundException(StudyRecordErrorCode errorCode)
    {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public StudyRecordErrorCode getErrorCode() {
        return errorCode;
    }


}
