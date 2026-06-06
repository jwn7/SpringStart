package com.springstart.study;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class StudyRecordExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {

        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(studyRecordErrorResponse);

    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleIllegalStateException(IllegalStateException e) {

        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(studyRecordErrorResponse);
    }

    @ExceptionHandler(StudyRecordNotFoundException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleStudyRecordNotFoundException(StudyRecordNotFoundException e) {
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(studyRecordErrorResponse);

    }
}
