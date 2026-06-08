package com.springstart.study;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudyRecordExceptionHandler {


    @ExceptionHandler(StudyRecordValidationException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleStudyRecordValidationException(StudyRecordValidationException e) {

        StudyRecordErrorCode errorCode = e.getErrorCode();
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(studyRecordErrorResponse);

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleIllegalStateException(IllegalStateException e) {

        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(studyRecordErrorResponse);
    }

    @ExceptionHandler(StudyRecordNotFoundException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleStudyRecordNotFoundException(StudyRecordNotFoundException e) {

        StudyRecordErrorCode errorCode = e.getErrorCode();
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(errorCode);


        return ResponseEntity.status(errorCode.getHttpStatus()).body(studyRecordErrorResponse);

    }
}
