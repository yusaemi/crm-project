package com.sample.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Company. 2020/11/20 12:44 下午
 *
 * @author sero
 * @version 1.0.0
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company")
@ApiModel(description = "company資訊")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "序號", required = true)
    private int id;

    @Column(nullable = false)
    @ApiModelProperty(value = "名稱", required = true)
    private String name;

    @ApiModelProperty(value = "地址")
    private String address;

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

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<Client> clients;

}
