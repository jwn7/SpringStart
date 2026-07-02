package com.springstart.study.repository;

import com.springstart.study.domain.StudyRecord;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public class JdbcStudyRecordRepository implements StudyRecordRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcStudyRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private StudyRecord mapToStudyRecord(ResultSet rs, int rowNum) throws SQLException {
        long recordId = rs.getLong("id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        int studyMinutes = rs.getInt("study_minutes");
        boolean completed = rs.getBoolean("completed");
        StudyRecord record = new StudyRecord(recordId, title, content, studyMinutes);

        if(completed){
            record.complete();
        }
        return record;
    }

    @Override
    public StudyRecord save(StudyRecord studyRecord) {
        jdbcTemplate.update("INSERT INTO study_records (id, title, content,study_minutes, completed) VALUES (?, ?, ?, ?, ?)",
                studyRecord.getId(),
                studyRecord.getTitle(),
                studyRecord.getContent(),
                studyRecord.getStudyMinutes(),
                studyRecord.isCompleted());
        return studyRecord;
    }

    @Override
    public Optional<StudyRecord> findById(Long id) {
        List<StudyRecord> records = jdbcTemplate.query("SELECT * FROM study_records WHERE id = ?",this::mapToStudyRecord,id);

        if(records.isEmpty())
            return Optional.empty();
        return Optional.of(records.get(0));
    }

    @Override
    public List<StudyRecord> findAll() {
        List<StudyRecord> list = jdbcTemplate.query("SELECT * FROM study_records ORDER BY id",this::mapToStudyRecord);
        return list;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM study_records WHERE id = ?", id);
    }

    @Override
    public void clear() {
        jdbcTemplate.update("DELETE FROM study_records");

    }
}
