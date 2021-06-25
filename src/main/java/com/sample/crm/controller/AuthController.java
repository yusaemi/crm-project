package com.sample.crm.controller;

import com.sample.crm.controller.request.UserRequest;
import com.sample.crm.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(tags = "Authentication Controller")
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "Authentication and get jwt")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "jwt已取得") })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
