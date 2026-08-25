package com.springstart.study.persistence;

import java.util.List;

public record StudyRecordCursorResponse(
        List<StudyRecordJpaResponse> contents,
        Long nextCursor,
        boolean hasNext
) {
}
