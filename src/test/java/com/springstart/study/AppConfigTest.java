package com.springstart.study;

import com.springstart.study.config.AppConfig;
import com.springstart.study.domain.StudyRecord;
import com.springstart.study.repository.JdbcStudyRecordRepository;
import com.springstart.study.service.StudyRecordService;
import com.springstart.study.web.StudyRecordController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.support.TestPropertySourceUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;


class AppConfigTest {
    private AnnotationConfigApplicationContext devContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, "study.default-minutes=30");
        context.getEnvironment().setActiveProfiles("dev");
        context.register(AppConfig.class);
        context.refresh();
        return context;
    }
    private AnnotationConfigApplicationContext jdbcContext()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, "study.default-minutes=30");
        context.getEnvironment().setActiveProfiles("jdbc");
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
    @Test
    void resourceTest() throws IOException {
        ClassPathResource resource = new ClassPathResource("study-default.txt");

        Assertions.assertTrue(resource.exists());
        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(inputStream.readAllBytes());
            Assertions.assertTrue(content.contains("default study minutes: 30"));
        }
    }

    @Test
    void controllerBeanTest(){
        AnnotationConfigApplicationContext ac = devContext();
        StudyRecordController controller = ac.getBean(StudyRecordController.class);

        Assertions.assertNotNull(controller);
        ac.close();
    }


    @Test
    void jdbcBeanTest()
    {
        AnnotationConfigApplicationContext ac = jdbcContext();
        DataSource dataSource = ac.getBean(DataSource.class);
        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
        JdbcStudyRecordRepository jdbcStudyRecordRepository = ac.getBean(JdbcStudyRecordRepository.class);

        Assertions.assertNotNull(dataSource);
        Assertions.assertNotNull(jdbcTemplate);
        Assertions.assertNotNull(jdbcStudyRecordRepository);
        ac.close();
    }

}
