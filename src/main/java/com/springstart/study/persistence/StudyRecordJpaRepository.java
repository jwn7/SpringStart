package com.springstart.study.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecordJpaRepository extends JpaRepository<StudyRecordEntity,Long> {
}
