package com.example.service;

import java.util.List;

import com.example.model.CategorySummary;
import com.example.model.DashboardSummary;
import com.example.model.Expense;
import com.example.repository.ExpenseRepository;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    public final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public void addExpense(Expense expense, int userid) {
        repository.save(expense, userid);
    }

    public List<Expense> getAllExpenses(int userid, LocalDate date) {
        return repository.getAllExpenses(userid, date);
    }

    public void deleteExpense(int id, int userid) {
        repository.deleteExpense(id, userid);
    }

    public void updateExpense(int id, Expense expense, int userid) {
        repository.updateExpense(id, expense, userid);
    }

    public DashboardSummary getSummary(int userId) {
        return repository.getSummary(userId);
    }

    public List<CategorySummary> getCategorySummary(int userId, LocalDate date) {

        return repository.getCategorySummary(userId, date);

    }

    public double getMonthlyExpenses(int userId, int year, int month) {
        return repository.getMonthlyExpenses(userId, year, month);
    }

}