package com.sample.crm.controller;

import com.sample.crm.controller.request.EmployeeRequest;
import com.sample.crm.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * AuthController. 2020/11/20 1:28 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication Controller")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authentication and get jwt")
    @ApiResponse(responseCode = "200", description = "jwt已取得")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid EmployeeRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
