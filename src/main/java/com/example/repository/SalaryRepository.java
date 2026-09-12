package com.example.repository;

import com.example.model.Salary;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class SalaryRepository {

    private final DataSource dataSource;

    public SalaryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Salary getSalary(int userId) {

        String query = "SELECT * FROM salary WHERE user_id = ?";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)
        ) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                Salary salary = new Salary();

                salary.setId(rs.getInt("id"));
                salary.setUserId(rs.getInt("user_id"));
                salary.setAmount(rs.getDouble("amount"));

                return salary;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveSalary(int userId, double amount) {

    String query = """
            INSERT INTO salary (user_id, amount)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE amount = ?
            """;

    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)
    ) {

        ps.setInt(1, userId);
        ps.setDouble(2, amount);
        ps.setDouble(3, amount);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

}