package com.sample.crm.controller;

import com.sample.crm.controller.request.CompanyRequest;
import com.sample.crm.entity.Company;
import com.sample.crm.service.CompanyService;
import com.sample.crm.service.dto.CompanyResponse;
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
 * CompanyController. 2020/11/22 2:35 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
@Api(tags = "Company Controller")
public class CompanyController {

    private final CompanyService companyService;

    @ApiOperation(value = "查詢company清單")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "company清單已取得") })
    @GetMapping("")
    public ResponseEntity<List<CompanyResponse>> getCompanies(){
        return ResponseEntity.ok(companyService.getCompanies());
    }

    @ApiOperation(value = "新增company")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "company資料已新增") })
    @PostMapping("")
    public ResponseEntity<Void> createCompany(@RequestBody @Valid CompanyRequest request) throws URISyntaxException {
        companyService.createCompany(request);
        return ResponseEntity.created(new URI("/")).build();
    }

    @ApiOperation(value = "查詢company")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "company資料已取得") })
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompany(@PathVariable("id") int id){
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @ApiOperation(value = "更新company")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "company資料已更新") })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompany(@PathVariable("id") int id, @RequestBody @Valid CompanyRequest request){
        companyService.updateCompany(id, request);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "刪除company")
    @ApiResponses(value = { @ApiResponse(code = 204, message = "company資料已刪除") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") int id){
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
