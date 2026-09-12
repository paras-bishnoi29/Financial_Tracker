package com.example.service;

import com.example.model.Salary;
import com.example.repository.SalaryRepository;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;

    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    public Salary getSalary(int userId) {
        return salaryRepository.getSalary(userId);
    }

    public void saveSalary(int userId, double amount) {
    salaryRepository.saveSalary(userId, amount);
}

}