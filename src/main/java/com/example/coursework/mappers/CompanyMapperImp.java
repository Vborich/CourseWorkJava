package com.example.coursework.mappers;

import com.example.coursework.dto.CompanyDto;
import com.example.coursework.models.Address;
import com.example.coursework.models.Company;

public class CompanyMapperImp implements CompanyMapper{
    @Override
    public Company toModel(CompanyDto companyDto) {
        if (companyDto == null)
            return null;
        Company company = CompanyMapper.INSTANCE.toModel(companyDto);
        Address address = new Address(companyDto.getCountry(), companyDto.getCity(),
                companyDto.getStreet(), companyDto.getBuilding());
        company.setAddress(address);
        address.setCompany(company);
        return company;
    }

    @Override
    public CompanyDto toDto(Company company) {
        if (company == null)
            return null;
        CompanyDto companyDto = CompanyMapper.INSTANCE.toDto(company);
        companyDto.setCountry(company.getAddress().getCountry());
        companyDto.setCity(company.getAddress().getCity());
        companyDto.setStreet(company.getAddress().getStreet());
        companyDto.setBuilding(company.getAddress().getBuilding());
        return  companyDto;
    }
}
