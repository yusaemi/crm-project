package com.sample.crm.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sample.crm.entity.Client;

/**
 * ClientResponse. 2020/11/22 4:20 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@JsonIgnoreProperties("company")
public class ClientResponse extends Client {
}
