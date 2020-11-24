package com.sample.crm;

import com.sample.crm.controller.request.ClientRequest;
import com.sample.crm.controller.request.CompanyRequest;
import com.sample.crm.entity.Client;
import com.sample.crm.entity.Company;
import com.sample.crm.repository.ClientDao;
import com.sample.crm.repository.CompanyDao;
import com.sample.crm.service.ClientService;
import com.sample.crm.service.CompanyService;
import com.sample.crm.service.dto.ClientResponse;
import com.sample.crm.service.dto.CompanyResponse;
import com.sample.crm.util.UserUtil;
import com.sample.crm.vo.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class CrmProjectApplicationTests {

	@Mock
	ClientDao clientDaoMock;

	@Mock
	CompanyDao companyDaoMock;

	@Mock
	UserUtil userUtilMock;

	@InjectMocks
	ClientService clientServiceMocks;

	@InjectMocks
	CompanyService companyServiceMocks;

	List<Client> clients;
	List<Company> companies;
	UserProfile userProfile;

	@BeforeEach
	public void beforeTest() {

		Client mockClient1 = Client.builder().id(1).companyId(2).name("mockClient1").build();
		Client mockClient2 = Client.builder().id(2).companyId(1).name("mockClient2").build();
		clients = Arrays.asList(mockClient1, mockClient2);

		Mockito.when(clientDaoMock.findAll()).thenReturn(clients);
		Mockito.when(clientDaoMock.findById(1)).thenReturn(Optional.of(mockClient1));
		Mockito.when(clientDaoMock.findById(3)).thenThrow(new RuntimeException("clientDaoMock mock throw exception"));

		Company mockCompany1 = Company.builder().id(1).name("mockCompany1").build();
		Company mockCompany2 = Company.builder().id(2).name("mockCompany2").build();
		companies = Arrays.asList(mockCompany1, mockCompany2);

		Mockito.when(companyDaoMock.findAll()).thenReturn(companies);
		Mockito.when(companyDaoMock.findById(1)).thenReturn(Optional.of(mockCompany1));
		Mockito.when(companyDaoMock.findById(3)).thenThrow(new RuntimeException("companyDaoMock mock throw exception"));

		Mockito.when(userUtilMock.getUserProfile()).thenReturn(
				userProfile = UserProfile.builder()
						.username("mockUsername")
						.password("mockPwd")
						.role("superuser").build()
		);



	}

	@Order(1)
	@DisplayName("ClientService - Test getClients")
	@Test
	void getClients() {
		List<ClientResponse> clientResponses = clientServiceMocks.getClients();
		verify(clientDaoMock, times(1)).findAll();
		Assertions.assertEquals(clientResponses, this.clients);
	}

	@Order(2)
	@DisplayName("ClientService - Test createClient")
	@Test
	void createClient() {
		clientServiceMocks.createClient(new ClientRequest());
		verify(clientDaoMock, times(1)).save(any());
	}

	@Order(3)
	@DisplayName("ClientService - Test getClient")
	@Test
	void getClient() {
		ClientResponse clientResponse = clientServiceMocks.getClient(1);
		try {
			clientServiceMocks.getClient(3);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("clientDaoMock mock throw exception", ex.getMessage());
		}
		verify(clientDaoMock, times(1)).findById(1);
		verify(clientDaoMock, times(1)).findById(3);
		Assertions.assertEquals(clientResponse, this.clients.get(0));
	}

	@Order(4)
	@DisplayName("ClientService - Test updateClient")
	@Test
	void updateClient() {
		clientServiceMocks.updateClient(1, new ClientRequest());
		try {
			clientServiceMocks.updateClient(3, new ClientRequest());
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("clientDaoMock mock throw exception", ex.getMessage());
		}
		verify(clientDaoMock, times(1)).findById(1);
		verify(clientDaoMock, times(1)).findById(3);
		verify(clientDaoMock, times(1)).save(any());
	}

	@Order(5)
	@DisplayName("ClientService - Test deleteClient")
	@Test
	void deleteClient() {
		clientServiceMocks.deleteClient(1);
		try {
			clientServiceMocks.deleteClient(3);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("clientDaoMock mock throw exception", ex.getMessage());
		}
		verify(clientDaoMock, times(1)).findById(1);
		verify(clientDaoMock, times(1)).findById(3);
		verify(clientDaoMock, times(1)).deleteById(1);
	}

	@Order(6)
	@DisplayName("ClientService - Test createClients")
	@Test
	void createClients() {
		List<ClientRequest> clientRequests = Arrays.asList(
				new ClientRequest(),
				new ClientRequest(),
				new ClientRequest()
		);
		clientServiceMocks.createClients(clientRequests);
		verify(clientDaoMock, times(3)).save(any());
	}

	@Order(7)
	@DisplayName("CompanyService - Test getCompanies")
	@Test
	void getCompanies() {
		List<CompanyResponse> companyResponses = companyServiceMocks.getCompanies();
		verify(companyDaoMock, times(1)).findAll();
		Assertions.assertEquals(companyResponses, this.companies);
	}

	@Order(8)
	@DisplayName("CompanyService - Test createCompany")
	@Test
	void createCompany() {
		companyServiceMocks.createCompany(new CompanyRequest());
		verify(companyDaoMock, times(1)).save(any());
	}

	@Order(9)
	@DisplayName("CompanyService - Test getCompany")
	@Test
	void getCompany() {
		CompanyResponse companyResponse = companyServiceMocks.getCompany(1);
		try {
			companyServiceMocks.getCompany(3);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("companyDaoMock mock throw exception", ex.getMessage());
		}
		verify(companyDaoMock, times(1)).findById(1);
		verify(companyDaoMock, times(1)).findById(3);
		Assertions.assertEquals(companyResponse, this.companies.get(0));
	}

	@Order(10)
	@DisplayName("CompanyService - Test updateCompany")
	@Test
	void updateCompany() {
		companyServiceMocks.updateCompany(1, new CompanyRequest());
		try {
			companyServiceMocks.updateCompany(3, new CompanyRequest());
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("companyDaoMock mock throw exception", ex.getMessage());
		}
		verify(companyDaoMock, times(1)).findById(1);
		verify(companyDaoMock, times(1)).findById(3);
		verify(companyDaoMock, times(1)).save(any());
	}

	@Order(11)
	@DisplayName("CompanyService - Test deleteCompany")
	@Test
	void deleteCompany() {
		companyServiceMocks.deleteCompany(1);
		try {
			companyServiceMocks.deleteCompany(3);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("companyDaoMock mock throw exception", ex.getMessage());
		}
		verify(companyDaoMock, times(1)).findById(1);
		verify(companyDaoMock, times(1)).findById(3);
		verify(companyDaoMock, times(1)).deleteById(1);
	}

	@Test
	void contextLoads() {
	}

}
