package com.example.springexample.service;

import com.example.springexample.model.Transaction;
import com.example.springexample.model.User;

import java.util.Map;
import java.util.stream.Collectors;

public class FinanceService {
    public void addIncome(User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Transaction transaction = new Transaction(category, amount, true);
        user.getWallet().addTransaction(transaction);
    }

    public void addExpense(User user, String category, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        Transaction transaction = new Transaction(category, amount, false);
        user.getWallet().addTransaction(transaction);
    }

    public void setBudget(User user, String category, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative");
        }
        user.getWallet().setBudget(category, amount);
    }

    public double getTotalIncome(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isExpenseOrIncome)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isExpenseOrIncome())
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Map<String, Double> getIncomeByCategory(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(Transaction::isExpenseOrIncome)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public Map<String, Double> getExpensesByCategory(User user) {
        return user.getWallet().getTransactions().stream()
                .filter(transaction -> !transaction.isExpenseOrIncome())
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    public double getBudgetRemaining(User user, String category) {
        double budget = user.getWallet().getBudget(category) != null ?
                user.getWallet().getBudget(category) : 0.0;
        double spent = getExpensesByCategory(user).getOrDefault(category, 0.0);
        return budget - spent;
    }

    public boolean isBudgetExceeded(User user, String category) {
        return getBudgetRemaining(user, category) < 0;
    }

    public boolean areExpensesExceedingIncome(User user) {
        return getTotalExpenses(user) > getTotalIncome(user);
    }
}
