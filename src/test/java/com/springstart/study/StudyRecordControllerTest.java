package com.springstart.study;

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

        mockMvc.perform(get("/records")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].title").value("Spring mvc")).andExpect(jsonPath("$[0].studyMinutes").value(30));
    }

    @Test
    void findByIdTest() throws Exception {
        service.create(1L, "Spring mvc", "mock mvc test", 30);

        mockMvc.perform(get("/records/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.title").value("Spring mvc")).andExpect(jsonPath("$.studyMinutes").value(30));

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
                .andExpect(jsonPath("$.studyMinutes").value(45));
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
                .andExpect(jsonPath("$.title").value("Updated MVC"))
                .andExpect(jsonPath("$.content").value("update test"))
                .andExpect(jsonPath("$.studyMinutes").value(60));
    }

    @Test
    void findByIdNotFoundTest() throws Exception {

        mockMvc.perform(get("/records/999")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("StudyRecord not found"));
    }

    @Test
    void IllegalStateTest() throws Exception {
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
                .andExpect(jsonPath("$.message").value("Title cannot be null or empty"));
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
                .andExpect(jsonPath("$.message").value("StudyMinutes cannot be less than 1"));
    }




}