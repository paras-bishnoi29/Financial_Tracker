package com.example.model;

public class CategorySummary {

    private String category;
    private double total;

    public CategorySummary() {
    }

    public CategorySummary(String category, double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}