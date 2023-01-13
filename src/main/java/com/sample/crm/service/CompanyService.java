package com.sample.crm.service;

import com.sample.crm.controller.request.CompanyRequest;
import com.sample.crm.entity.Company;
import com.sample.crm.repository.CompanyDao;
import com.sample.crm.service.dto.CompanyResponse;
import com.sample.crm.util.UserUtil;
import com.sample.crm.vo.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CompanyService. 2020/11/22 2:36 上午
 *
 * @author sero
 * @version 1.0.0
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CompanyService {

    private final CompanyDao companyDao;
    private final UserUtil userUtil;

    public List<CompanyResponse> getCompanies() {
        List<Company> companies = companyDao.findAll();
        return companies.stream().map(company -> {
            CompanyResponse companyResponse = new CompanyResponse();
            BeanUtils.copyProperties(company, companyResponse);
            return companyResponse;
        }).toList();
    }

    public void createCompany(CompanyRequest request) {
        UserProfile userProfile = userUtil.getUserProfile();
        Company company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .createdBy(userProfile.getUsername())
                .build();
        companyDao.save(company);
    }

    public CompanyResponse getCompany(int id) {
        Company company = companyDao.findById(id).orElseThrow(() -> new RuntimeException("company is not exist!"));
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        return companyResponse;
    }

    public void updateCompany(int id, CompanyRequest request) {
        UserProfile userProfile = userUtil.getUserProfile();
        Company company = companyDao.findById(id).orElseThrow(() -> new RuntimeException("company is not exist!"));
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setUpdatedBy(userProfile.getUsername());
        company.setUpdatedAt(LocalDateTime.now());
        companyDao.save(company);
    }

    public void deleteCompany(int id) {
        companyDao.findById(id).ifPresent(companyDao::delete);
    }

}
