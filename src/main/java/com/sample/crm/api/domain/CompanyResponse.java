package com.sample.crm.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * CompanyResponse. 2020/11/22 4:10 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyResponse {

    @Schema(name = "序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;

    @Schema(name = "名稱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "地址")
    private String address;

    @Schema(name = "建立人員")
    private String createdBy;

    @Schema(name = "建立時間")
    private LocalDateTime createdAt;

    @Schema(name = "更新人員")
    private String updatedBy;

    @Schema(name = "更新時間")
    private LocalDateTime updatedAt;

}
