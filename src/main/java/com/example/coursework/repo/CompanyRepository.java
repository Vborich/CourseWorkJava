package com.example.coursework.repo;

import com.example.coursework.models.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository  extends CrudRepository<Company, Long> {
    Company findByCompanyName(String companyName);
}
