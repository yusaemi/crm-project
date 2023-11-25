package com.sample.crm;

import com.sample.crm.api.domain.ClientRequest;
import com.sample.crm.api.domain.CompanyRequest;
import com.sample.crm.dao.entity.Client;
import com.sample.crm.dao.entity.Company;
import com.sample.crm.dao.repository.ClientDao;
import com.sample.crm.dao.repository.CompanyDao;
import com.sample.crm.api.service.ClientService;
import com.sample.crm.api.service.CompanyService;
import com.sample.crm.api.domain.ClientResponse;
import com.sample.crm.api.domain.CompanyResponse;
import com.sample.crm.util.UserUtil;
import com.sample.crm.system.domain.UserProfile;
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
	private ClientDao clientDaoMock;

	@Mock
	private CompanyDao companyDaoMock;

	@Mock
	private UserUtil userUtilMock;

	@InjectMocks
	private ClientService clientServiceMocks;

	@InjectMocks
	private CompanyService companyServiceMocks;

	private List<Client> clients;
	private List<Company> companies;

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

		Mockito.when(userUtilMock.get()).thenReturn(
				UserProfile.builder()
						.username("mockUsername")
						.password("mockPwd")
						.role("superuser").build()
		);



	}

	@Order(1)
	@DisplayName("ClientService - Test getClients")
	@Test
	void getClients() {
		List<Integer> clientResponseIds = clientServiceMocks.getClients().stream().map(ClientResponse::getId).toList();
		verify(clientDaoMock, times(1)).findAll();
		Assertions.assertIterableEquals(clientResponseIds, clients.stream().map(Client::getId).toList());
	}

	@Order(2)
	@DisplayName("ClientService - Test createClient")
	@Test
	void createClient() {
		clientServiceMocks.createClient(new ClientRequest());
		verify(clientDaoMock, times(1)).save(any(Client.class));
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
		Assertions.assertEquals(clientResponse.getId(), clients.get(0).getId());
	}

	@Order(4)
	@DisplayName("ClientService - Test updateClient")
	@Test
	void updateClient() {
		ClientRequest clientRequest = new ClientRequest();
		clientServiceMocks.updateClient(1, clientRequest);
		try {
			clientServiceMocks.updateClient(3, clientRequest);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("clientDaoMock mock throw exception", ex.getMessage());
		}
		verify(clientDaoMock, times(1)).findById(1);
		verify(clientDaoMock, times(1)).findById(3);
		verify(clientDaoMock, times(1)).save(any(Client.class));
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
		verify(clientDaoMock, times(1)).delete(any(Client.class));
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
		verify(clientDaoMock, times(3)).save(any(Client.class));
	}

	@Order(7)
	@DisplayName("CompanyService - Test getCompanies")
	@Test
	void getCompanies() {
		List<Integer> companyResponseId = companyServiceMocks.getCompanies().stream().map(CompanyResponse::getId).toList();
		verify(companyDaoMock, times(1)).findAll();
		Assertions.assertIterableEquals(companyResponseId, companies.stream().map(Company::getId).toList());
	}

	@Order(8)
	@DisplayName("CompanyService - Test createCompany")
	@Test
	void createCompany() {
		companyServiceMocks.createCompany(new CompanyRequest());
		verify(companyDaoMock, times(1)).save(any(Company.class));
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
		Assertions.assertEquals(companyResponse.getId(), companies.get(0).getId());
	}

	@Order(10)
	@DisplayName("CompanyService - Test updateCompany")
	@Test
	void updateCompany() {
		CompanyRequest companyRequest = new CompanyRequest();
		companyServiceMocks.updateCompany(1, companyRequest);
		try {
			companyServiceMocks.updateCompany(3, companyRequest);
			Assertions.fail();
		} catch (RuntimeException ex) {
			Assertions.assertEquals("companyDaoMock mock throw exception", ex.getMessage());
		}
		verify(companyDaoMock, times(1)).findById(1);
		verify(companyDaoMock, times(1)).findById(3);
		verify(companyDaoMock, times(1)).save(any(Company.class));
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
		verify(companyDaoMock, times(1)).delete(any(Company.class));
	}

}
