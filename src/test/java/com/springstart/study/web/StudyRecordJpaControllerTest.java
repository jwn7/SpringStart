package com.springstart.study.web;

import com.springstart.study.exception.StudyRecordExceptionHandler;
import com.springstart.study.persistence.StudyRecordEntity;
import com.springstart.study.persistence.StudyRecordJpaRepository;
import com.springstart.study.service.StudyRecordJpaService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DataJpaTest
@Import(StudyRecordJpaService.class)
class StudyRecordJpaControllerTest {

    @Autowired
    private StudyRecordJpaService studyRecordJpaService;

    @Autowired
    private StudyRecordJpaRepository studyRecordJpaRepository;

    @Autowired
    private EntityManager em;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new StudyRecordJpaController(studyRecordJpaService)
                )
                .setControllerAdvice(StudyRecordExceptionHandler.class)
                .build();
    }

    @Test
    void controllerTest() throws Exception {
        StudyRecordEntity studyRecordEntity = initEntity();

        mockMvc.perform(get("/jpa-records").param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents[0].studyMinutes").value(90))
                .andExpect(jsonPath("$.contents[1].studyMinutes").value(60))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.nextCursor").value(studyRecordEntity.getId()));
    }

    @Test
    void invalidSizeTest() throws Exception {
        mockMvc.perform(get("/jpa-records").param("size", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.errors[0].field").value("size"))
                .andExpect(jsonPath("$.errors[0].errorCode").value("INVALID_PAGE_SIZE"));
    }

    @Test
    void invalidSizeTypeTest() throws Exception {
        mockMvc.perform(get("/jpa-records").param("size", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errorCode").value("INVALID_REQUEST_PARAMETER"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

    }

    @Test
    void nextCursorRequestReturnsLastPage() throws Exception {
        initEntity();

        MvcResult firstResult = mockMvc.perform(get("/jpa-records").param("size", "2"))
                .andExpect(status().isOk())
                .andReturn();
        String json = firstResult.getResponse().getContentAsString();


        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        long nextCursor = root.get("nextCursor").asLong();

        mockMvc.perform(get("/jpa-records")
                .param("cursorId", String.valueOf(nextCursor))
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents.length()").value(1))
                .andExpect(jsonPath("$.contents[0].studyMinutes").value(30))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.nextCursor").doesNotExist());

    }

    private StudyRecordEntity initEntity() {
        StudyRecordEntity studyRecordEntity = new StudyRecordEntity("title", "t", 30);
        studyRecordJpaRepository.save(studyRecordEntity);
        StudyRecordEntity studyRecordEntity1 = new StudyRecordEntity("title1", "t", 60);
        studyRecordJpaRepository.save(studyRecordEntity1);
        StudyRecordEntity studyRecordEntity2 = new StudyRecordEntity("title2", "t", 90);
        studyRecordJpaRepository.save(studyRecordEntity2);

        em.flush();
        em.clear();

        return studyRecordEntity1;
    }
}