package com.sample.crm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.crm.entity.Company;

/**
 * CompanyResponse. 2020/11/22 4:10 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@JsonIgnoreProperties("clients")
public class CompanyResponse extends Company {
}
