package com.springstart.study;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        FieldError fieldError = e.getFieldError();
        String fieldName = fieldError.getField();
        StudyRecordErrorCode errorCode = switch(fieldName)
        {
            case "title" -> StudyRecordErrorCode.INVALID_TITLE;
            case "studyMinutes" -> StudyRecordErrorCode.INVALID_STUDY_MINUTES;
            default -> throw new IllegalStateException("Unexpected validation field");
        };
        StudyRecordErrorResponse studyRecordErrorResponse = new StudyRecordErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(studyRecordErrorResponse);


    }
}
