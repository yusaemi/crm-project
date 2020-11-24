package com.sample.crm.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Client. 2020/11/20 12:51 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
@ApiModel(description = "client資訊")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "序號", required = true)
    private int id;

    @Column(name = "company_id", nullable = false)
    @ApiModelProperty(value = "compant序號", required = true)
    private int companyId;

    @Column(nullable = false)
    @ApiModelProperty(value = "姓名", required = true)
    private String name;

    @ApiModelProperty(value = "電子郵件")
    private String email;

    @ApiModelProperty(value = "聯絡電話")
    private String phone;

    @Column(name = "created_by")
    @ApiModelProperty(value = "建立人員")
    private String createdBy;

    @Column(name = "created_at")
    @ApiModelProperty(value = "建立時間")
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    @ApiModelProperty(value = "更新人員")
    private String updatedBy;

    @Column(name = "updated_at")
    @ApiModelProperty(value = "更新時間")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private Company company;

}
