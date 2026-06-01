package com.springstart.study;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;


class AppConfigTest {
    private AnnotationConfigApplicationContext devContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, "study.default-minutes=30");
        context.getEnvironment().setActiveProfiles("dev");
        context.register(AppConfig.class);
        context.refresh();
        return context;
    }
    @Test
    void contextLoads() {
        AnnotationConfigApplicationContext context = devContext();
        StudyRecordService service = context.getBean(StudyRecordService.class);
        StudyRecord studyRecord = service.create(1L, "Spring Bean", "config test", 30);

        Assertions.assertNotNull(service);
        Assertions.assertEquals("Spring Bean", studyRecord.getTitle());

        context.close();
    }

    @Test
    void singletonBeanTest()
    {
        AnnotationConfigApplicationContext context = devContext();
        StudyRecordService service = context.getBean(StudyRecordService.class);
        StudyRecordService service2 = context.getBean(StudyRecordService.class);

        Assertions.assertSame(service,service2);
        context.close();
    }


    @Test
    void profileTest()
    {
        AnnotationConfigApplicationContext ac = devContext();

        StudyRecordService service = ac.getBean(StudyRecordService.class);
        StudyRecord studyRecord = service.create(1L, "Profile", "dev profile test", 30);

        Assertions.assertNotNull(studyRecord);

        ac.close();
    }
    @Test
    void propertiesTest()
    {
        AnnotationConfigApplicationContext ac = devContext();
        Integer value = ac.getEnvironment().getProperty("study.default-minutes", Integer.class);
        StudyRecordService service = ac.getBean(StudyRecordService.class);
        StudyRecord studyRecord = service.create(1L, "Profile", "dev profile test", value);

        Assertions.assertNotNull(value);
        Assertions.assertEquals(30, studyRecord.getStudyMinutes());

    }

}