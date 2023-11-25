package com.sample.crm.api.service;

import com.sample.crm.api.domain.ClientRequest;
import com.sample.crm.api.domain.ClientResponse;
import com.sample.crm.dao.entity.Client;
import com.sample.crm.dao.repository.ClientDao;
import com.sample.crm.util.UserUtil;
import com.sample.crm.system.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClientService. 2020/11/22 4:22 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClientService {

    private final ClientDao clientDao;
    private final UserUtil userUtil;

    public List<ClientResponse> getClients() {
        List<Client> clients = clientDao.findAll();
        return clients.stream().map(client -> {
            ClientResponse clientResponse = new ClientResponse();
            BeanUtils.copyProperties(client, clientResponse);
            return clientResponse;
        }).toList();
    }

    public void createClient(ClientRequest request) {
        UserProfile userProfile = userUtil.get();
        Client client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .companyId(request.getCompanyId())
                .createdBy(userProfile.getUsername())
                .build();
        clientDao.save(client);
    }

    public ClientResponse getClient(int id) {
        Client client = clientDao.findById(id).orElseThrow(() -> new RuntimeException("client is not exist!"));
        ClientResponse clientResponse = new ClientResponse();
        BeanUtils.copyProperties(client, clientResponse);
        return clientResponse;
    }

    public void updateClient(int id, ClientRequest request) {
        UserProfile userProfile = userUtil.get();
        Client client = clientDao.findById(id).orElseThrow(() -> new RuntimeException("client is not exist!"));
        client.setName(request.getName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setCompanyId(request.getCompanyId());
        client.setUpdatedBy(userProfile.getUsername());
        client.setUpdatedAt(LocalDateTime.now());
        clientDao.save(client);
    }

    public void deleteClient(int id) {
        clientDao.findById(id).ifPresent(clientDao::delete);
    }

    public void createClients(List<ClientRequest> request) {
        request.forEach(this::createClient);
    }

}
