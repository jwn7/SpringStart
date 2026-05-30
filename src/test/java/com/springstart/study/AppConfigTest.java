package com.springstart.study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class AppConfigTest {

    @Test
    void contextLoads() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        StudyRecordService service = context.getBean(StudyRecordService.class);
        StudyRecord studyRecord = service.create(1L, "Spring Bean", "config test", 30);

        Assertions.assertNotNull(service);
        Assertions.assertEquals("Spring Bean", studyRecord.getTitle());

        context.close();
    }

    @Test
    void singletonBeanTest()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        StudyRecordService service = context.getBean(StudyRecordService.class);
        StudyRecordService service2 = context.getBean(StudyRecordService.class);

        Assertions.assertSame(service,service2);
        context.close();
    }



}