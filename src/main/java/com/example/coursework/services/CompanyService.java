package com.example.coursework.services;

import com.example.coursework.models.Company;
import com.example.coursework.models.Status.StatusEnum;
import com.example.coursework.models.User;
import com.example.coursework.repo.CompanyRepository;
import com.example.coursework.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean addCompany(Company company)
    {
        Company companyDb = companyRepository.findByCompanyName(company.getCompanyName());
        if (companyDb != null)
            return  false;
        companyRepository.save(company);
        return true;
    }

    public boolean createCompany(Company company, long userId)
    {
        Company companyDb = companyRepository.findByCompanyName(company.getCompanyName());
        if (companyDb != null)
            return  false;

        User user = userRepository.findById(userId).get();
        user.setCompany(company);
        company.setUserOwner(user);
        companyRepository.save(company);
        return true;
    }

    public boolean editCompany(Company company)
    {
        Company companyDb = companyRepository.findByCompanyName(company.getCompanyName());
        if (companyDb != null && companyDb.getId() != company.getId())
            return  false;

        companyDb = getCompanyById(company.getId());
        companyDb.setCompanyName(company.getCompanyName());
        companyDb.setCentralEmail(company.getCentralEmail());
        companyDb.setCentralPhoneNumber(company.getCentralPhoneNumber());
        company.getAddress().setCompany(companyDb);
        company.getAddress().setId(companyDb.getId());
        companyDb.setAddress(company.getAddress());
        companyRepository.save(companyDb);
        return true;
    }

    public Iterable<Company> getCompanies()
    {
        return companyRepository.findAll();
    }

    public boolean removeCompany(Long id)
    {
        if (!companyRepository.existsById(id))
            return false;
        companyRepository.deleteById(id);
        return true;
    }

    public Company getCompanyById(Long id)
    {
        if (!companyRepository.existsById(id))
            return null;
        return companyRepository.findById(id).get();
    }

    public StatusEnum addUserToCompany(long companyId, long userId)
    {
        Company company = getCompanyById(companyId);
        if (company == null)
            return StatusEnum.BadCompany;

        User user = userRepository.findById(userId).get();
        if (user == null)
            return StatusEnum.BadUser;
        if (user.getCompany() != null)
            return StatusEnum.BadUserCompany;
        if (company.getUserOwner() == null)
            company.setUserOwner(user);

        user.setCompany(company);
        userRepository.save(user);
        return StatusEnum.Successfully;
    }

    public boolean setUserOwner(long idCompany, long idUser)
    {
        Company company = getCompanyById(idCompany);
        User user = userRepository.findById(idUser).get();
        if (user == null || company == null)
            return false;

        company.setUserOwner(user);
        companyRepository.save(company);
        return true;
    }

    public boolean changeCompanyForUser(long idCompany, long idUser)
    {
        Company company = getCompanyById(idCompany);
        User user = userRepository.findById(idUser).get();
        if (user == null || company == null)
            return false;

        if (company.getUserOwner() == null)
            company.setUserOwner(user);

        user.setCompany(company);
        userRepository.save(user);
        return true;
    }
}
