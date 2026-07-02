package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
@Profile("dev")
public class InMemoryStudyRecordRepository implements StudyRecordRepository
{
    private final Map<Long, StudyRecord> store = new HashMap<>();

    public StudyRecord save(StudyRecord record) {
        store.put(record.getId(), record);
        return record;
    }

    public Optional<StudyRecord> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<StudyRecord> findAll() {
        return new ArrayList<>(store.values());
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

    public void clear() {
        store.clear();
    }
}
