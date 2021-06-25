package com.sample.crm.controller;

import com.sample.crm.controller.request.ClientRequest;
import com.sample.crm.entity.Client;
import com.sample.crm.service.ClientService;
import com.sample.crm.service.dto.ClientResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@Api(tags = "Client Controller")
public class ClientController {

    private final ClientService clientService;

    @ApiOperation(value = "查詢client清單")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "client清單已取得") })
    @GetMapping("")
    public ResponseEntity<List<ClientResponse>> getClients(){
        return ResponseEntity.ok(clientService.getClients());
    }

    @ApiOperation(value = "新增client")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "client資料已新增") })
    @PostMapping("")
    public ResponseEntity<Void> createClient(@RequestBody @Valid ClientRequest request) throws URISyntaxException {
        clientService.createClient(request);
        return ResponseEntity.created(new URI("/")).build();
    }

    @ApiOperation(value = "查詢client")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "client資料已取得") })
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClient(@PathVariable("id") int id){
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @ApiOperation(value = "更新client")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "client資料已更新") })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable("id") int id, @RequestBody @Valid ClientRequest request){
        clientService.updateClient(id, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "刪除client")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "client資料已刪除") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id") int id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "新增多筆client")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "多筆client資料已新增") })
    @PostMapping("/collection")
    public ResponseEntity<Void> createClients(@RequestBody @Valid List<ClientRequest> request) throws URISyntaxException {
        clientService.createClients(request);
        return ResponseEntity.created(new URI("/")).build();
    }

}
