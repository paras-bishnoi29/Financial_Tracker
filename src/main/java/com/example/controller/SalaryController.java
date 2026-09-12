package com.example.controller;

import com.example.model.Salary;
import com.example.service.SalaryService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/salary")
    public Salary getSalary(HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return null;
        }

        return salaryService.getSalary(userId);
    }

    @PostMapping("/salary")
    public void saveSalary(
            @RequestBody Salary salary,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return;
        }

        salaryService.saveSalary(userId, salary.getAmount());
    }
}