package com.springstart.study;

import com.springstart.study.domain.StudyRecord;
import com.springstart.study.exception.StudyRecordExceptionHandler;
import com.springstart.study.exception.StudyRecordNotFoundException;
import com.springstart.study.repository.InMemoryStudyRecordRepository;
import com.springstart.study.repository.StudyRecordRepository;
import com.springstart.study.service.StudyRecordService;
import com.springstart.study.web.StudyRecordController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class StudyRecordControllerTest {


    StudyRecordRepository repository = new InMemoryStudyRecordRepository();
    StudyRecordService service = new StudyRecordService(repository);
    StudyRecordController controller = new StudyRecordController(service);

    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(StudyRecordExceptionHandler.class)
            .build();

    @Test
    void recordTest() throws Exception {
        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(get("/records")).andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].id").value(1))
                .andExpect(jsonPath("$.contents[0].title").value("Spring mvc"))
                .andExpect(jsonPath("$.contents[0].content").value("mock mvc test"))
                .andExpect(jsonPath("$.contents[0].studyMinutes").value(30))
                .andExpect(jsonPath("$.contents[0].completed").value(false))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    void findByIdTest() throws Exception {
        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(get("/records/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring mvc"))
                .andExpect(jsonPath("$.content").value("mock mvc test"))
                .andExpect(jsonPath("$.studyMinutes").value(30))
                .andExpect(jsonPath("$.completed").value(false));

    }

    @Test
    void createTest() throws Exception {
        mockMvc.perform(post("/records").contentType(MediaType.APPLICATION_JSON).content("""
                        {
                        "id": 2,
                        "title" : "Post Mvc",
                        "content" : "post test",
                        "studyMinutes" : 45
                        }"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Post Mvc"))
                .andExpect(jsonPath("$.content").value("post test"))
                .andExpect(jsonPath("$.studyMinutes").value(45))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void deleteTest() throws Exception {
        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(delete("/records/1"))
                .andExpect(status().isNoContent());

        Assertions.assertThrows(StudyRecordNotFoundException.class,
                () -> service.findById(1L)
        );
    }

    @Test
    void updateTest() throws Exception {
        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(put("/records/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "title": "Updated MVC",
                                    "content": "update test",
                                    "studyMinutes": 60
                                }
                                """))
                .andExpect((status().isOk()))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated MVC"))
                .andExpect(jsonPath("$.content").value("update test"))
                .andExpect(jsonPath("$.studyMinutes").value(60))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void findByIdNotFoundTest() throws Exception {

        mockMvc.perform(get("/records/999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.errorCode").value("STUDY_RECORD_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("StudyRecord not found"));
    }

    @Test
    void completedRecordTest() throws Exception {
        //given
        StudyRecord record = service.create(1L, "Spring mvc", "mock mvc test", 30);
        record.complete();

        //when

        mockMvc.perform(put("/records/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "title" : "Updated MVC",
                            "content": "update test",
                            "studyMinutes": 60
                        }       
                        """
                ))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode").value("STUDY_RECORD_ALREADY_COMPLETED"))
                .andExpect(jsonPath("$.message").value("This record is already completed"));

        //then

    }

    @Test
    void createWithBlankTitleTest() throws Exception {
        mockMvc.perform(post("/records").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 3,
                                    "title": "",
                                    "content": "bad request test",
                                    "studyMinutes": 30
                                }  
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("title"))
                .andExpect(jsonPath("$.errors[0].errorCode").value("INVALID_TITLE"))
                .andExpect(jsonPath("$.errors[0].message").value("Title cannot be null or empty"));
    }

    @Test
    void createWithInvalidStudyMinutesTest() throws Exception {

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 3,
                                    "title": "bad request test",
                                    "content": "bad request test",
                                    "studyMinutes": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.errors[0].field").value("studyMinutes"))
                .andExpect(jsonPath("$.errors[0].errorCode").value("INVALID_STUDY_MINUTES"))
                .andExpect(jsonPath("$.errors[0].message").value("StudyMinutes cannot be less than 1"));
    }

    @Test
    void multipleInvalidTest() throws Exception {

        mockMvc.perform(post("/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {
                                    "id": 3,
                                    "title": "",
                                    "content": "bad request test",
                                    "studyMinutes": 0
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(2));
    }

    @Test
    void invalidRequestTest() throws Exception {

        mockMvc.perform(post("/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 3,
                                    "title": "title",
                                    "content": "bad request test",
                                    "studyMinutes": "abc"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid Request body"));
    }

    @Test
    void invalidRangeTest() throws Exception {

        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(get("/records?page=2&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()").value(0))
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.hasNext").value(false));
    }

    @Test
    void paginationTest() throws Exception {

        service.create(1L, "Spring mvc", "mock mvc test", 30);
        service.create(2L, "Spring mvc", "mock mvc test", 30);
        service.create(3L, "Spring mvc", "mock mvc test", 30);
        service.create(4L, "Spring mvc", "mock mvc test", 30);
        service.create(5L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(get("/records?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()").value(2))
                .andExpect(jsonPath("$.contents[0].id").value(3))
                .andExpect(jsonPath("$.contents[1].id").value(2))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.hasNext").value(true));


    }

    @Test
    void negativePageTest() throws Exception {

        mockMvc.perform(get("/records?page=-1&size=2"))
                .andExpect(status().isBadRequest());
    }
}

