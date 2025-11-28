package org.pokeherb.driverservice.driver.domain.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.driverservice.driver.domain.application.command.DriverCommandService;
import org.pokeherb.driverservice.driver.domain.application.dto.DriverCreateReqeustDto;
import org.pokeherb.driverservice.driver.domain.application.query.DriverQueryService;
import org.pokeherb.driverservice.driver.domain.entity.DriverType;
import org.pokeherb.driverservice.driver.domain.entity.dto.DriverDto;
import org.pokeherb.driverservice.driver.domain.exception.DriverErrorCode;
import org.pokeherb.driverservice.global.infrastructure.exception.CustomException;
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
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DriverControllerTest {

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
    @DisplayName(value = "드라이버 삭제")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void deleteDriver() throws Exception{
        // given
        UUID driverId = UUID.randomUUID();

        mockMvc.perform(delete("/{driverId}", driverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 드라이버 삭제 시 404 반환")
    @WithMockUser(username = "testUser", roles = {"MASTER"})
    void deleteDriver_NotFound() throws Exception {
        // given
        UUID driverId = UUID.randomUUID();

        // 수정 포인트: 구체적인 driverId 변수 대신 any() 혹은 eq()를 사용하여 매칭 범위를 안전하게 잡음
        doThrow(new CustomException(DriverErrorCode.DRIVER_NOT_FOUND))
                .when(driverCommandService).deleteDriver(any(UUID.class));

        // when & then
        mockMvc.perform(delete("/{driverId}", driverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }
}