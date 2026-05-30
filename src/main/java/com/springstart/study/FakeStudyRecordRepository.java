package com.springstart.study;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FakeStudyRecordRepository implements StudyRecordRepository {
    @Override
    public StudyRecord save(StudyRecord studyRecord) {

        throw new UnsupportedOperationException("Fake repository does not support save");
    }

    @Override
    public Optional<StudyRecord> findById(Long id) {
        throw new UnsupportedOperationException("Fake repository does not support findById");

    }

    @Override
    public List<StudyRecord> findAll() {
        throw new UnsupportedOperationException("Fake repository does not support findAll");

    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Fake repository does not support deleteById");

    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Fake repository does not support clear");

    }
}
