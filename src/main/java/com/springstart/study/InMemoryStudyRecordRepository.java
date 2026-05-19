package com.springstart.study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryStudyRecordRepository {
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
