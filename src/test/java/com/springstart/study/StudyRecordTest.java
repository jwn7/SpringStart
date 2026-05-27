package com.springstart.study;

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
        Assertions.assertThrows(IllegalArgumentException.class, () -> new StudyRecord(1L, "", "domain test", 30));
    }
    @Test
    public void zeroStudyMinutesTest()
    {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new StudyRecord(1L, "ttt", "domain test", 0));
    }
    @Test
    public void completedRecordTest()
    {
        StudyRecord studyRecord = new StudyRecord(1L, "JAVA OOP", "domain test", 30);
        studyRecord.complete();
        Assertions.assertThrows(IllegalStateException.class, () -> studyRecord.update("test","TEST",  312));

    }
}
