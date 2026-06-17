package com.springstart.study.exception;

import com.springstart.study.web.response.StudyRecordErrorResponse;
import com.springstart.study.web.response.StudyRecordFieldErrorResponse;
import com.springstart.study.web.response.StudyRecordValidationErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<StudyRecordValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<StudyRecordFieldErrorResponse> errors = new ArrayList<>();

        for (FieldError fieldError : fieldErrors) {

            String fieldName = fieldError.getField();

            StudyRecordErrorCode errorCode = switch(fieldName)
            {
                case "title" -> StudyRecordErrorCode.INVALID_TITLE;
                case "studyMinutes" -> StudyRecordErrorCode.INVALID_STUDY_MINUTES;
                default -> StudyRecordErrorCode.VALIDATION_FAILED;
            };
            errors.add(new StudyRecordFieldErrorResponse(fieldName, errorCode));
        }
        StudyRecordValidationErrorResponse response = new StudyRecordValidationErrorResponse(StudyRecordErrorCode.VALIDATION_FAILED, errors);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StudyRecordErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        StudyRecordErrorResponse response = new StudyRecordErrorResponse(StudyRecordErrorCode.INVALID_REQUEST);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
