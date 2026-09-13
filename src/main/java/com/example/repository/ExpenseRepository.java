package com.example.repository;

import com.example.model.CategorySummary;
import com.example.model.DashboardSummary;
import com.example.model.Expense;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.sql.DataSource;

@Repository
public class ExpenseRepository {
    private final DataSource dataSource;

    public ExpenseRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Expense expense, int userid) {
        String sql = "Insert into expenses (name, amount, category, user_id) values (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, expense.getName());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setInt(4, userid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getAllExpenses(int userid, LocalDate date) {
        List<Expense> expenses = new ArrayList<>();
        String query;
        if (date == null) {
        query = "SELECT * FROM expenses WHERE user_id = ?";
    } else {
        query = "SELECT * FROM expenses WHERE user_id = ? AND expense_date = ?";
    }

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userid);
            if (date != null) {
            ps.setDate(2, java.sql.Date.valueOf(date));
        }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Expense expense = new Expense();
                expense.setId(rs.getInt("id"));
                expense.setName(rs.getString("name"));
                expense.setAmount(rs.getDouble("amount"));
                expense.setCategory(rs.getString("category"));
                expense.setExpenseDate(
                rs.getDate("expense_date").toLocalDate()
            );

                expenses.add(expense);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenses;
    }

    public void deleteExpense(int id, int userid) {
        String query = "delete from expenses where id = ? and user_id = ?";
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.setInt(2, userid);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateExpense(int id, Expense expense, int userid) {
        String query = "update expenses set name = ?, amount = ? , category = ? where id = ? and user_id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, expense.getName());
            ps.setDouble(2, expense.getAmount());
            ps.setString(3, expense.getCategory());
            ps.setInt(4, id);
            ps.setInt(5, userid);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DashboardSummary getSummary(int useid) {
        String query = "select sum(amount) as totalExpense, count(*) as totalTransactions, max(amount) as highestExpense from expenses where user_id = ?;";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, useid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DashboardSummary summary = new DashboardSummary();

                summary.setTotalExpense(rs.getDouble("totalExpense"));
                summary.setTotalTransactions(rs.getInt("totalTransactions"));
                summary.setHighestExpense(rs.getDouble("highestExpense"));

                return summary;
            }
            return new DashboardSummary();
        } catch (Exception e) {
            e.printStackTrace();
            return new DashboardSummary();
        }
    }

    public List<CategorySummary> getCategorySummary(int userId, LocalDate date) {

    String query;

    if (date == null) {
        query = """
                SELECT category, SUM(amount) AS total
                FROM expenses
                WHERE user_id = ?
                GROUP BY category
                """;
    } else {
        query = """
                SELECT category, SUM(amount) AS total
                FROM expenses
                WHERE user_id = ?
                AND expense_date = ?
                GROUP BY category
                """;
    }

    List<CategorySummary> result = new ArrayList<>();

    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)
    ) {

        ps.setInt(1, userId);

        if (date != null) {
            ps.setDate(2, java.sql.Date.valueOf(date));
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            CategorySummary summary = new CategorySummary();

            summary.setCategory(rs.getString("category"));
            summary.setTotal(rs.getDouble("total"));

            result.add(summary);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return result;
}

public double getMonthlyExpenses(int userId, int year, int month) {

    String query = """
            SELECT COALESCE(SUM(amount), 0)
            FROM expenses
            WHERE user_id = ?
            AND YEAR(expense_date) = ?
            AND MONTH(expense_date) = ?
            """;

    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)
    ) {

        ps.setInt(1, userId);
        ps.setInt(2, year);
        ps.setInt(3, month);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getDouble(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

}