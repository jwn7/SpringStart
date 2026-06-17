package com.springstart.study;

import com.springstart.study.config.AppConfig;
import com.springstart.study.domain.StudyRecord;
import com.springstart.study.exception.StudyRecordAlreadyCompletedException;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.service.StudyRecordService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StudyRecordApp {
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        StudyRecordService service = context.getBean(StudyRecordService.class);

        StudyRecord record = service.create(1L,"java OOP","service flow practice", 30);
        System.out.println("created id = " + record.getId());
        System.out.println("created title = " + record.getTitle());
        System.out.println("created content = " + record.getContent());
        System.out.println("created studyMinutes = " + record.getStudyMinutes());
        System.out.println("created completed = " + record.isCompleted());

        StudyRecord updateRecord = service.update(1L, "Java service", "Update flow practice", 45);


        System.out.println("updated id = " + record.getId());
        System.out.println("updated title = " + record.getTitle());
        System.out.println("updated content = " + record.getContent());
        System.out.println("updated studyMinutes = " + record.getStudyMinutes());
        System.out.println("same object after update = " + (record == updateRecord));

        StudyRecord completedRecord = service.complete(1L);
        System.out.println("completed = " + record.isCompleted());
        System.out.println("same object after complete = " + (completedRecord == record));

        try{
            service.update(1L,"After complete", "Shoud fail", 10);
        }
        catch(StudyRecordAlreadyCompletedException e)
        {
            System.out.println("update after complete error = " + e.getMessage());
        }
        service.delete(1L);

        try {
            service.findById(1L);
        } catch (StudyRecordNotFoundException e) {
            System.out.println("find after delete error = " + e.getMessage());
        }
        context.close();
    }

}
