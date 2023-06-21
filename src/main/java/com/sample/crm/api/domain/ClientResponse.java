package com.sample.crm.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ClientResponse. 2020/11/22 4:20 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

    @Schema(name = "序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;

    @Schema(name = "company序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int companyId;

    @Schema(name = "姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "電子郵件")
    private String email;

    @Schema(name = "聯絡電話")
    private String phone;

    @Schema(name = "建立人員")
    private String createdBy;

    @Schema(name = "建立時間")
    private LocalDateTime createdAt;

    @Schema(name = "更新人員")
    private String updatedBy;

    @Schema(name = "更新時間")
    private LocalDateTime updatedAt;

}
