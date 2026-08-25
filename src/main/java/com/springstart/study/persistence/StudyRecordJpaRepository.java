package com.springstart.study.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRecordJpaRepository extends JpaRepository<StudyRecordEntity,Long> {

    List<StudyRecordEntity> findByTitle(String title);

    List<StudyRecordEntity> findByTitleOrderByStudyMinutesDesc(String title);

    Optional<StudyRecordEntity> findFirstByTitleOrderByStudyMinutesDesc(String title);

    List<StudyRecordEntity> findByStudyMinutesGreaterThan(int studyMinutes);

    @Query("""
        SELECT r
        FROM StudyRecordEntity r
        WHERE r.title = :title
        AND r.studyMinutes > :minimumStudyMinutes
        ORDER BY r.studyMinutes desc

""")
    List<StudyRecordEntity> searchByTitleAndMinimumStudyMinutes(@Param("title") String title, @Param("minimumStudyMinutes")  int minimumStudyMinutes);


    @Query("""
        SELECT new com.springstart.study.persistence.StudyRecordSummary(
        r.id,
        r.title,
        r.studyMinutes
        )
        from StudyRecordEntity r
        where r.title = :title
        order by r.studyMinutes desc

""")
    List<StudyRecordSummary> findSummariesByTitle(@Param("title")String title);

    Slice<StudyRecordEntity> findByCompleted(
            boolean completed,
            Pageable pageable
    );


    @Query("""
                    SELECT r
                    from StudyRecordEntity r
                    where (:cursorId is NULL OR r.id < :cursorId)
                    order by r.id desc
            """)
    List<StudyRecordEntity> findNextByCursor(
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
