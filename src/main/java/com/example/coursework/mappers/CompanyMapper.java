package com.example.coursework.mappers;

import com.example.coursework.dto.CompanyDto;
import com.example.coursework.models.Company;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);
    Company toModel(CompanyDto companyDto);
    CompanyDto toDto(Company company);
}
