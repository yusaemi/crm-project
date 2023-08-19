package com.sample.crm.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.crm.dao.entity.Company;

/**
 * CreateCompany. 2020/11/22 3:52 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@JsonIgnoreProperties({ "id", "createdBy", "createdAt", "createdBy", "updatedBy", "updatedAt", "clients" })
public class CompanyRequest extends Company {
}
