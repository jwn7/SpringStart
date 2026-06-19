package com.springstart.study.web.response;


import java.util.List;
public class StudyRecordPageResponse {

    List<StudyRecordResponse> contents;
    int page;
    int size;
    int totalElements;
    int totalPages;
    boolean hasNext;

    public StudyRecordPageResponse(List<StudyRecordResponse> contents, int page, int size, int totalElements, int totalPages, boolean hasNext) {
        this.contents = contents;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public List<StudyRecordResponse> getContents() {
        return contents;
    }
    public int getPage() {
        return page;
    }
    public int getSize() {
        return size;
    }
    public int getTotalElements() {
        return totalElements;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public boolean isHasNext() {
        return hasNext;
    }

}
