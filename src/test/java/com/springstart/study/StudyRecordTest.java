package com.springstart.study;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.exception.StudyRecordAlreadyCompletedException;
import com.springstart.study.exception.StudyRecordValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StudyRecordTest {


    @Test
    public void createTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L, "JAVA OOP", "domain test", 30);

        Assertions.assertEquals(1L, studyRecord.getId());
        Assertions.assertEquals("JAVA OOP", studyRecord.getTitle());
        Assertions.assertEquals("domain test", studyRecord.getContent());
        Assertions.assertEquals(30, studyRecord.getStudyMinutes());
        Assertions.assertFalse(studyRecord.isCompleted());
    }

    @Test
    public void nullTitleTest()
    {
        Assertions.assertThrows(StudyRecordValidationException.class, () -> new StudyRecord(1L, "", "domain test", 30));
    }
    @Test
    public void zeroStudyMinutesTest()
    {
        Assertions.assertThrows(StudyRecordValidationException.class, () -> new StudyRecord(1L, "ttt", "domain test", 0));
    }
    @Test
    public void completedRecordTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L, "JAVA OOP", "domain test", 30);
        studyRecord.complete();
        Assertions.assertThrows(StudyRecordAlreadyCompletedException.class, () -> studyRecord.update("test","TEST",  312));

    }
}
