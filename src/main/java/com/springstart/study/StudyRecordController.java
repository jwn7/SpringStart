package com.springstart.study;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/records")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    public StudyRecordController(StudyRecordService studyRecordService) {
        this.studyRecordService = studyRecordService;
    }

    @GetMapping
    public List<StudyRecordResponse> findAll() {
        return studyRecordService.findAll().stream()
                .map(records -> new StudyRecordResponse(records)).toList();
    }

    @GetMapping("/{id}")
    public StudyRecordResponse findById(@PathVariable("id") Long id) {

        return new StudyRecordResponse(studyRecordService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudyRecordResponse> create(@Valid @RequestBody CreateStudyRecordRequest request) {
        StudyRecord record = studyRecordService.create(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getStudyMinutes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new StudyRecordResponse(record));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id)
    {
        studyRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public StudyRecordResponse update(@PathVariable("id") Long id,@Valid @RequestBody UpdateStudyRecordRequest request) {
        return new StudyRecordResponse(studyRecordService.update(id, request.getTitle(), request.getContent(), request.getStudyMinutes()));
    }

}
