package com.example.springexample.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private String category;
    private double amount;
    private boolean ExpenseOrIncome;
    private LocalDateTime transactionDate;

    public Transaction(String category, double amount, boolean expenseOrIncome) {
        this.category = category;
        this.amount = amount;
        this.ExpenseOrIncome = expenseOrIncome;
        this.transactionDate = LocalDateTime.now();
    }

    public Transaction() {
        this.transactionDate = LocalDateTime.now();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isExpenseOrIncome() {
        return ExpenseOrIncome;
    }

    public void setExpenseOrIncome(boolean expenseOrIncome) {
        ExpenseOrIncome = expenseOrIncome;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(amount, that.amount) == 0 && ExpenseOrIncome == that.ExpenseOrIncome && Objects.equals(category, that.category) && Objects.equals(transactionDate, that.transactionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, amount, ExpenseOrIncome, transactionDate);
    }
}
