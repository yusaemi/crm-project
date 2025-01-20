package com.sample.crm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.crm.api.domain.EmployeeRequest;
import com.sample.crm.dao.entity.Client;
import com.sample.crm.dao.entity.Employee;
import com.sample.crm.dao.repository.ClientDao;
import com.sample.crm.dao.repository.EmployeeDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * CrmProjectApplicationWebTest. 2020/11/22 2:44 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrmProjectApplicationWebTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @Autowired
    public CrmProjectApplicationWebTest(ObjectMapper objectMapper, MockMvc mockMvc) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
    }

    @MockitoBean
    private EmployeeDao employeeDaoMock;

    @MockitoBean
    private ClientDao clientDaoMock;

    private String superuserJwt;
    private String managerJwt;
    private String operatorJwt;
    private Client client;

    private final String header = "Authorization";
    private final String jwtPrefix = "Bearer ";

    @BeforeEach
    public void beforeTest() throws Exception {

        final String superuser = "superuser";
        final String manager = "manager";
        final String operator = "operator";

        Employee superEmployee = Employee.builder()
                .username(superuser)
                .password("$2a$12$2JhdvLvF4yqAQ5Dntc7P.OEuo7QJeZJNpjT/yAIsxjzBKZVwrk7k6")
                .role(superuser)
                .build();
        Employee managerEmployee = Employee.builder()
                .username(manager)
                .password("$2a$12$iOXbZfWuAB.3fE3IOQ7cVuAXY7c8.p6JNkb7MiF31JI07qImL7ybm")
                .role(manager)
                .build();
        Employee operatorEmployee = Employee.builder()
                .username(operator)
                .password("$2a$12$MzLp/30NfG/gCLH.bPKuyeKq2ECPq5jAXbax0w66IePYm2zcfw4cK")
                .role(operator)
                .build();
        
        when(employeeDaoMock.findById(superuser)).thenReturn(Optional.of(superEmployee));
        when(employeeDaoMock.findById(manager)).thenReturn(Optional.of(managerEmployee));
        when(employeeDaoMock.findById(operator)).thenReturn(Optional.of(operatorEmployee));

        client = Client.builder().id(1).companyId(1).name("mockClient1").build();
        when(clientDaoMock.findById(1)).thenReturn(Optional.of(client));

        MvcResult superuserResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EmployeeRequest.builder()
                                .username(superuser)
                                .password("pwd")
                                .build()))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        MvcResult managerResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EmployeeRequest.builder()
                                .username(manager)
                                .password("pwd")
                                .build()))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        MvcResult operatorResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(EmployeeRequest.builder()
                                .username(operator)
                                .password("pwd")
                                .build()))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        superuserJwt = superuserResult.getResponse().getContentAsString();
        managerJwt = managerResult.getResponse().getContentAsString();
        operatorJwt = operatorResult.getResponse().getContentAsString();

    }

    @Order(1)
    @DisplayName("Permission - Test view permission")
    @Test
    void permissionViewTest() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.get("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + superuserJwt)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + managerJwt)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + operatorJwt)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();

    }

    @SuppressWarnings("squid:S5778")
    @Order(2)
    @DisplayName("Permission - Test modify permission")
    @Test
    void permissionModifyTest() throws Exception {

        String clientJson = objectMapper.writeValueAsString(client);

        MvcResult superuserPutClientResult = mockMvc.perform(
                MockMvcRequestBuilders.put("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + superuserJwt)
                        .param("id", "1")
                        .content(clientJson)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.put("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + managerJwt)
                        .param("id", "1")
                        .content(clientJson)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.put("/clients/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(header, jwtPrefix + operatorJwt)
                            .param("id", "1")
                            .content(clientJson)
            ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();
            Assertions.fail();
        } catch (Error e) {
            Assertions.assertEquals("Status expected:<204> but was:<403>", e.getMessage());
        }
    }

    @SuppressWarnings("squid:S5778")
    @Order(3)
    @DisplayName("Permission - Test delete permission")
    @Test
    void permissionDeleteTest() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + superuserJwt)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/clients/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + managerJwt)
        ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.delete("/clients/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(header, jwtPrefix + operatorJwt)
            ).andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print()).andReturn();
            Assertions.fail();
        } catch (Error e) {
            Assertions.assertEquals("Status expected:<204> but was:<403>", e.getMessage());
        }
    }

    @SuppressWarnings("squid:S5778")
    @Order(4)
    @DisplayName("Permission - Test create permission")
    @Test
    void permissionCreateTest() throws Exception {

        String clientJson = objectMapper.writeValueAsString(client);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/clients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + superuserJwt)
                        .content(clientJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print()).andReturn();

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/clients")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(header, jwtPrefix + managerJwt)
                            .content(clientJson)
            ).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print()).andReturn();
            Assertions.fail();
        } catch (Error e) {
            Assertions.assertEquals("Status expected:<201> but was:<403>", e.getMessage());
        }

        mockMvc.perform(
                MockMvcRequestBuilders.post("/clients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(header, jwtPrefix + operatorJwt)
                        .content(clientJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print()).andReturn();

    }

}
