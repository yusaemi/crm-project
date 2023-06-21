package com.sample.crm.api.controller;

import com.sample.crm.api.domain.CompanyRequest;
import com.sample.crm.api.domain.CompanyResponse;
import com.sample.crm.api.service.CompanyService;
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
 * CompanyController. 2020/11/22 2:35 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/companies")
@Tag(name = "Company Controller")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "查詢company清單")
    @ApiResponse(responseCode = "200", description = "company清單已取得")
    @GetMapping("")
    public ResponseEntity<List<CompanyResponse>> getCompanies() {
        return ResponseEntity.ok(companyService.getCompanies());
    }

    @Operation(summary = "新增company")
    @ApiResponse(responseCode = "201", description = "company資料已新增")
    @PostMapping("")
    public ResponseEntity<Void> createCompany(@RequestBody @Valid CompanyRequest request) throws URISyntaxException {
        companyService.createCompany(request);
        return ResponseEntity.created(new URI("/")).build();
    }

    @Operation(summary = "查詢company")
    @ApiResponse(responseCode = "200", description = "company資料已取得")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable("id") int id) {
        return ResponseEntity.ok(companyService.getCompany(id));
    }

    @Operation(summary = "更新company")
    @ApiResponse(responseCode = "204", description = "company資料已更新")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCompany(@PathVariable("id") int id, @RequestBody @Valid CompanyRequest request) {
        companyService.updateCompany(id, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "刪除company")
    @ApiResponse(responseCode = "204", description = "company資料已刪除")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") int id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

}
