package com.sample.crm.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Employee. 2020/11/21 1:04 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
@Schema(name = "employee資訊")
public class Employee implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @Schema(name = "帳號", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Column(nullable = false)
    @Schema(name = "密碼", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Column(nullable = false)
    @Schema(name = "所屬角色", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

}
