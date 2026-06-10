package com.springstart.study;

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

    @ExceptionHandler(StudyRecordAlreadyCompletedException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleStudyRecordAlreadyCompletedException(StudyRecordAlreadyCompletedException e) {

        StudyRecordErrorCode errorCode = e.getErrorCode();
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(studyRecordErrorResponse);
    }

    @ExceptionHandler(StudyRecordNotFoundException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleStudyRecordNotFoundException(StudyRecordNotFoundException e) {

        StudyRecordErrorCode errorCode = e.getErrorCode();
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(errorCode);


        return ResponseEntity.status(errorCode.getHttpStatus()).body(studyRecordErrorResponse);

    }
}
