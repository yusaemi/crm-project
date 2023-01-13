package com.sample.crm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Client. 2020/11/20 12:51 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
@Schema(name = "client資訊")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;

    @Column(name = "company_id", nullable = false)
    @Schema(name = "company序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int companyId;

    @Column(nullable = false)
    @Schema(name = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "電子郵件")
    private String email;

    @Schema(name = "聯絡電話")
    private String phone;

    @Column(name = "created_by")
    @Schema(name = "建立人員")
    private String createdBy;

    @Column(name = "created_at")
    @Schema(name = "建立時間")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    @Schema(name = "更新人員")
    private String updatedBy;

    @Column(name = "updated_at")
    @Schema(name = "更新時間")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

}
