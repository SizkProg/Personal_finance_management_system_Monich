package com.example.springexample;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.springexample.model.User;
import com.example.springexample.service.FinanceService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FinanceServiceTest {

    private FinanceService financeService;
    private User testUser;

    @BeforeEach
    public void setUp() {
        financeService = new FinanceService();
        testUser = new User("alex", "securePass789");
    }

    @Test
    public void testAddIncome() {
        // Добавляем доход
        assertDoesNotThrow(() -> financeService.addIncome(testUser, "Фриланс", 75000.0));

        // Проверяем, что доход добавлен
        assertEquals(75000.0, financeService.getTotalIncome(testUser), 0.01);
        assertEquals(0.0, financeService.getTotalExpenses(testUser), 0.01);
    }

    @Test
    public void testAddExpense() {
        // Добавляем расход
        assertDoesNotThrow(() -> financeService.addExpense(testUser, "Аренда", 25000.0));

        // Проверяем, что расход добавлен
        assertEquals(0.0, financeService.getTotalIncome(testUser), 0.01);
        assertEquals(25000.0, financeService.getTotalExpenses(testUser), 0.01);
    }

    @Test
    public void testAddNegativeAmount() {
        // Проверяем, что нельзя добавить отрицательный доход
        assertThrows(IllegalArgumentException.class, () -> {
            financeService.addIncome(testUser, "Инвестиции", -15000.0);
        });

        // Проверяем, что нельзя добавить отрицательный расход
        assertThrows(IllegalArgumentException.class, () -> {
            financeService.addExpense(testUser, "Развлечения", -3000.0);
        });
    }

    @Test
    public void testSetBudget() {
        // Устанавливаем бюджет
        assertDoesNotThrow(() -> financeService.setBudget(testUser, "Продукты", 15000.0));

        // Проверяем, что бюджет установлен
        Map<String, Double> budgets = testUser.getWallet().getBudgets();
        assertTrue(budgets.containsKey("Продукты"));
        assertEquals(15000.0, budgets.get("Продукты"), 0.01);
    }

    @Test
    public void testSetNegativeBudget() {
        // Проверяем, что нельзя установить отрицательный бюджет
        assertThrows(IllegalArgumentException.class, () -> {
            financeService.setBudget(testUser, "Транспорт", -8000.0);
        });
    }

    @Test
    public void testGetIncomeByCategory() {
        // Добавляем доходы
        financeService.addIncome(testUser, "Основная работа", 90000.0);
        financeService.addIncome(testUser, "Дивиденды", 12000.0);
        financeService.addIncome(testUser, "Основная работа", 10000.0);

        // Проверяем доходы по категориям
        Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(testUser);
        assertEquals(2, incomeByCategory.size());
        assertEquals(100000.0, incomeByCategory.get("Основная работа"), 0.01);
        assertEquals(12000.0, incomeByCategory.get("Дивиденды"), 0.01);
    }

    @Test
    public void testGetExpensesByCategory() {
        // Добавляем расходы
        financeService.addExpense(testUser, "Образование", 35000.0);
        financeService.addExpense(testUser, "Здоровье", 8000.0);
        financeService.addExpense(testUser, "Образование", 5000.0);

        // Проверяем расходы по категориям
        Map<String, Double> expensesByCategory = financeService.getExpensesByCategory(testUser);
        assertEquals(2, expensesByCategory.size());
        assertEquals(40000.0, expensesByCategory.get("Образование"), 0.01);
        assertEquals(8000.0, expensesByCategory.get("Здоровье"), 0.01);
    }

    @Test
    public void testGetBudgetRemaining() {
        // Устанавливаем бюджет
        financeService.setBudget(testUser, "Авто", 20000.0);

        // Добавляем расходы
        financeService.addExpense(testUser, "Авто", 8500.0);
        financeService.addExpense(testUser, "Авто", 4500.0);

        // Проверяем оставшийся бюджет
        assertEquals(7000.0, financeService.getBudgetRemaining(testUser, "Авто"), 0.01);
    }

    @Test
    public void testIsBudgetExceeded() {
        // Устанавливаем бюджет
        financeService.setBudget(testUser, "Отпуск", 50000.0);

        // Добавляем расходы, превышающие бюджет
        financeService.addExpense(testUser, "Отпуск", 35000.0);
        financeService.addExpense(testUser, "Отпуск", 25000.0);

        // Проверяем, что бюджет превышен
        assertTrue(financeService.isBudgetExceeded(testUser, "Отпуск"));
    }

    @Test
    public void testAreExpensesExceedingIncome() {
        // Добавляем доходы и расходы, где расходы больше доходов
        financeService.addIncome(testUser, "Проекты", 60000.0);
        financeService.addExpense(testUser, "Ипотека", 40000.0);
        financeService.addExpense(testUser, "Ремонт", 35000.0);

        // Проверяем, что расходы превышают доходы
        assertTrue(financeService.areExpensesExceedingIncome(testUser));
    }

    @Test
    public void testGetAllFinancialData() {
        // Создаем комплексные данные
        financeService.addIncome(testUser, "Основная работа", 80000.0);
        financeService.addIncome(testUser, "Подработка", 20000.0);
        financeService.addExpense(testUser, "Жилье", 40000.0);
        financeService.addExpense(testUser, "Питание", 15000.0);
        financeService.addExpense(testUser, "Транспорт", 5000.0);
        financeService.setBudget(testUser, "Питание", 20000.0);
        financeService.setBudget(testUser, "Транспорт", 8000.0);

        // Проверяем структурированные данные
        Map<String, Double> incomeByCategory = financeService.getIncomeByCategory(testUser);
        Map<String, Double> expensesByCategory = financeService.getExpensesByCategory(testUser);

        assertEquals(2, incomeByCategory.size());
        assertEquals(3, expensesByCategory.size());
        assertEquals(80000.0, incomeByCategory.get("Основная работа"), 0.01);
        assertEquals(20000.0, incomeByCategory.get("Подработка"), 0.01);
        assertEquals(15000.0, expensesByCategory.get("Питание"), 0.01);

        // Проверяем оставшийся бюджет
        assertEquals(5000.0, financeService.getBudgetRemaining(testUser, "Питание"), 0.01);
        assertEquals(3000.0, financeService.getBudgetRemaining(testUser, "Транспорт"), 0.01);

    }

    @Test
    public void testUpdateBudget() {
        // Устанавливаем первоначальный бюджет
        financeService.setBudget(testUser, "Развлечения", 10000.0);

        // Проверяем первоначальный бюджет
        assertEquals(10000.0, testUser.getWallet().getBudgets().get("Развлечения"), 0.01);

        // Обновляем бюджет
        financeService.setBudget(testUser, "Развлечения", 15000.0);

        // Проверяем обновленный бюджет
        assertEquals(15000.0, testUser.getWallet().getBudgets().get("Развлечения"), 0.01);

    }

    @Test

    public void testMultipleBudgets() {
        // Устанавливаем бюджеты для разных категорий
        financeService.setBudget(testUser, "Еда", 20000.0);
        financeService.setBudget(testUser, "Транспорт", 10000.0);
        financeService.setBudget(testUser, "Развлечения", 5000.0);

        Map<String, Double> budgets = testUser.getWallet().getBudgets();

        assertEquals(3, budgets.size());
        assertTrue(budgets.containsKey("Еда"));
        assertTrue(budgets.containsKey("Транспорт"));
        assertTrue(budgets.containsKey("Развлечения"));
        assertEquals(20000.0, budgets.get("Еда"), 0.01);
        assertEquals(10000.0, budgets.get("Транспорт"), 0.01);
        assertEquals(5000.0, budgets.get("Развлечения"), 0.01);

    }
}