package com.sample.crm.api.controller;

import com.sample.crm.api.domain.ClientRequest;
import com.sample.crm.api.domain.ClientResponse;
import com.sample.crm.api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * ClientController. 2020/11/22 4:20 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
@Tag(name = "Client Controller")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "查詢client清單")
    @ApiResponse(responseCode = "200", description = "client清單已取得")
    @GetMapping("")
    public ResponseEntity<List<ClientResponse>> getClients() {
        return ResponseEntity.ok(clientService.getClients());
    }

    @Operation(summary = "新增client")
    @ApiResponse(responseCode = "201", description = "client資料已新增")
    @PostMapping("")
    public ResponseEntity<Void> createClient(@RequestBody @Valid ClientRequest request) throws URISyntaxException {
        clientService.createClient(request);
        return ResponseEntity.created(new URI("/")).build();
    }

    @Operation(summary = "查詢client")
    @ApiResponse(responseCode = "200", description = "client資料已取得")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable("id") int id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @Operation(summary = "更新client")
    @ApiResponse(responseCode = "204", description = "client資料已更新")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable("id") int id, @RequestBody @Valid ClientRequest request) {
        clientService.updateClient(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "刪除client")
    @ApiResponse(responseCode = "204", description = "client資料已刪除")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") int id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "新增多筆client")
    @ApiResponse(responseCode = "201", description = "多筆client資料已新增")
    @PostMapping("/collection")
    public ResponseEntity<Void> createClients(@RequestBody @Valid List<ClientRequest> request) throws URISyntaxException {
        clientService.createClients(request);
        return ResponseEntity.created(new URI("/")).build();
    }

}
