package com.example.coursework.services;

import com.example.coursework.models.Company;
import com.example.coursework.models.User;
import com.example.coursework.repo.CompanyRepository;
import com.example.coursework.repo.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CompanyServiceTest {

    @Autowired
    CompanyService companyService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CompanyRepository companyRepository;

    @Test
    void setUserOwnerForCompany() {
        User oldUserOwner = new User();
        oldUserOwner.setId(55L);

        User user = new User();
        user.setId(1L);

        Company company = new Company();
        company.setId(1L);
        company.setUserOwner(oldUserOwner);

        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(true).when(userRepository).existsById(1L);
        Mockito.doReturn(Optional.of(company)).when(companyRepository).findById(1L);
        Mockito.doReturn(true).when(companyRepository).existsById(1L);

        boolean isSetUserOwner = companyService.setUserOwnerForCompany(1, 1);

        Assert.assertTrue(isSetUserOwner);
        Assert.assertTrue(company.getUserOwner().getId().equals(user.getId()));

        Mockito.verify(companyRepository, Mockito.times(1)).save(company);
    }

    @Test
    void setUserOwnerForCompanyWithNotExistUser() {
        User oldUserOwner = new User();
        oldUserOwner.setId(55L);

        Company company = new Company();
        company.setId(1L);
        company.setUserOwner(oldUserOwner);

        Mockito.doReturn(Optional.of(company)).when(companyRepository).findById(1L);
        Mockito.doReturn(true).when(companyRepository).existsById(1L);

        boolean isSetUserOwner = companyService.setUserOwnerForCompany(1, 1);

        Assert.assertFalse(isSetUserOwner);
        Assert.assertTrue(company.getUserOwner().getId().equals(oldUserOwner.getId()));

        Mockito.verify(companyRepository, Mockito.times(0)).save(company);
    }
}