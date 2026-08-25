package com.springstart.study.web;

import com.springstart.study.persistence.StudyRecordCursorResponse;
import com.springstart.study.service.StudyRecordJpaService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa-records")
public class StudyRecordJpaController {

    private final StudyRecordJpaService studyRecordJpaService;

    public StudyRecordJpaController(StudyRecordJpaService studyRecordJpaService) {
        this.studyRecordJpaService = studyRecordJpaService;
    }

    @GetMapping
    public StudyRecordCursorResponse findNextByCursor(
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) int size
    ) {
        return studyRecordJpaService.findNextByCursor(cursorId, size);
    }

}
