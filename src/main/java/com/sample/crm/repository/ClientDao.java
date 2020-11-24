package com.sample.crm.repository;

import com.sample.crm.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ClientDao. 2020/11/20 1:20 下午
 *
 * @author sero
 * @version 1.0.0
 **/
public interface ClientDao extends JpaRepository<Client, Integer> {
}
