package com.sample.crm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Company. 2020/11/20 12:44 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
@Schema(name = "company資訊")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "序號", requiredMode = Schema.RequiredMode.REQUIRED)
    private int id;

    @Column(nullable = false)
    @Schema(name = "名稱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(name = "地址")
    private String address;

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

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Client> clients;

}
