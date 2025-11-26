package org.pokeherb.driverservice.driver.domain.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.driverservice.driver.domain.application.command.DriverCommandService;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverCreateReqeustDto;
import org.pokeherb.driverservice.driver.domain.application.query.DriverQueryService;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DriverCommandService driverCommandService;

    @MockitoBean
    private DriverQueryService driverQueryService;

    @Test
    void getDriver() {
    }

    @Test
    @DisplayName(value = "드라이버 생성")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void createDriver() throws Exception{

        // given
        Long hubId = 1L;
        UUID slackId = UUID.randomUUID();
        String name = "testName";
        DriverType type = DriverType.HUB_DRIVER;

        DriverCreateReqeustDto requestDto = new DriverCreateReqeustDto(hubId, slackId, name, type);
        DriverDto responseDto = DriverDto.builder()
                .hubId(hubId)
                .slackId(slackId)
                .name(name)
                .driverType(type)
                .build();

        given(driverCommandService.createDriver(any(DriverCreateReqeustDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isCreated())
        ;


    }

    @Test
    void updateDriverType() {
    }

    @Test
    void deleteDriver() {
    }
}