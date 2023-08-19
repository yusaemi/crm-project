package com.sample.crm.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.crm.dao.entity.Client;

/**
 * ClientRequest. 2020/11/22 4:20 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@JsonIgnoreProperties({ "id", "createdBy", "createdAt", "createdBy", "updatedBy", "updatedAt", "company" })
public class ClientRequest extends Client {
}
