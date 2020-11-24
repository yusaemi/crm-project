package com.sample.crm.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User. 2020/11/21 1:04 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@ApiModel(description = "user資訊")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @ApiModelProperty(value = "帳號", required = true)
    private String username;

    @Column(nullable = false)
    @ApiModelProperty(value = "密碼", required = true)
    private String password;

    @Column(nullable = false)
    @ApiModelProperty(value = "所屬角色", required = true)
    private String role;

}
