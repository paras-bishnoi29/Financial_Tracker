package com.example.controller;

import com.example.model.CategorySummary;
import com.example.model.DashboardSummary;
import com.example.model.Expense;
import com.example.service.ExpenseService;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
public class ExpenseController {

    public final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expense")
    public String addexpense(@RequestBody Expense expense, HttpSession session) {
        Integer userid = (Integer) session.getAttribute("userId");
        if (userid == null) {
            return "Please Login First";
        }
        expenseService.addExpense(expense, userid);
        return "Expense Recieved";
    }

    @GetMapping("/expense")
    public List<Expense> getExpenses(
            @RequestParam(required = false) String date,
            HttpSession session) {

        Integer userid = (Integer) session.getAttribute("userId");

        if (userid == null) {
            return new ArrayList<>();
        }

        LocalDate expenseDate = null;

        if (date != null && !date.isBlank()) {
            expenseDate = LocalDate.parse(date);
        }

        return expenseService.getAllExpenses(userid, expenseDate);
    }

    @DeleteMapping("/expense/{id}")
    public String deleteExpense(@PathVariable int id, HttpSession session) {
        Integer userid = (Integer) session.getAttribute("userId");
        if (userid == null) {
            return "Please Login First";
        }
        expenseService.deleteExpense(id, userid);
        return "Deleted";
    }

    @PutMapping("/expense/{id}")
    public String updateExpense(@PathVariable int id, @RequestBody Expense expense, HttpSession session) {
        Integer userid = (Integer) session.getAttribute("userId");
        if (userid == null) {
            return "Please Login First";
        }
        expenseService.updateExpense(id, expense, userid);
        return "Expense Updated";
    }

    @GetMapping("/summary")
    public DashboardSummary getSummary(HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return new DashboardSummary();
        }

        return expenseService.getSummary(userId);
    }

    @GetMapping("/category-summary")
    public List<CategorySummary> getCategorySummary(
            @RequestParam(required = false) String date,
            HttpSession session) {

        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return new ArrayList<>();
        }

        LocalDate categoryDate = null;

        if (date != null && !date.isBlank()) {
            categoryDate = LocalDate.parse(date);
        }

        return expenseService.getCategorySummary(userId, categoryDate);
    }

    @GetMapping("/expense/monthly")
public double getCurrentMonthExpenses(HttpSession session) {

    Integer userId = (Integer) session.getAttribute("userId");

    if (userId == null) {
        return 0;
    }

    return expenseService.getCurrentMonthExpenses(userId);
}

}
