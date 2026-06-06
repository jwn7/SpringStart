package com.springstart.study;

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
    public List<StudyRecord> findAll() {
        return studyRecordService.findAll();
    }

    @GetMapping("/{id}")
    public StudyRecord findById(@PathVariable("id") Long id) {
        return studyRecordService.findById(id);
    }

    @PostMapping
    public ResponseEntity<StudyRecord> create(@RequestBody CreateStudyRecordRequest request) {
        StudyRecord record = studyRecordService.create(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getStudyMinutes()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id)
    {
        studyRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public StudyRecord update(@PathVariable("id") Long id, @RequestBody UpdateStudyRecordRequest request) {
        return studyRecordService.update(id, request.getTitle(), request.getContent(), request.getStudyMinutes());
    }

}
