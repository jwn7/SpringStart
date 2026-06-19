package com.springstart.study.web;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.service.StudyRecordService;
import com.springstart.study.web.request.CreateStudyRecordRequest;
import com.springstart.study.web.request.UpdateStudyRecordRequest;
import com.springstart.study.web.response.StudyRecordPageResponse;
import com.springstart.study.web.response.StudyRecordResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RestController
@RequestMapping("/records")
public class StudyRecordController {

    private final StudyRecordService studyRecordService;

    public StudyRecordController(StudyRecordService studyRecordService) {
        this.studyRecordService = studyRecordService;
    }
    @GetMapping
    public StudyRecordPageResponse findAll(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {

        List<StudyRecordResponse> contents = studyRecordService.findAll().stream()
                .sorted(Comparator.comparing(StudyRecord::getId).reversed())
                .map(records -> new StudyRecordResponse(records)).toList();

        int start = page * size;
        int end = Math.min((start + size), contents.size());
        int totalElements = contents.size(); // 전체 데이터 개수
        int totalPages = (totalElements + size - 1) / size; // 총 페이지 수
        boolean hasNext = (page + 1) < totalPages;

        List<StudyRecordResponse> pageResponses = new ArrayList<>();
        if (start < totalElements) {
                pageResponses = new ArrayList<>(contents.subList(start, end));
        }
        return new StudyRecordPageResponse(pageResponses, page, size, totalElements, totalPages, hasNext);
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
