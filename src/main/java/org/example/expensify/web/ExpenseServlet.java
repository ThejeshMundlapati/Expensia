package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.example.expensify.dao.CategoryDAO;
import org.example.expensify.dao.ExpenseDAO;
import org.example.expensify.dao.PreferenceDAO;
import org.example.expensify.model.Preference;

@WebServlet(urlPatterns = {
    "/dashboard",
    "/expense/add",
    "/expense/delete",
    "/expense/edit",
    "/expense/update",
    "/expenses"
})
public class ExpenseServlet extends HttpServlet {

  private final ExpenseDAO expenseDAO = new ExpenseDAO();
  private final CategoryDAO categoryDAO = new CategoryDAO();
  private final PreferenceDAO prefDAO = new PreferenceDAO();

  private int uid(HttpServletRequest req) {
    Object o = req.getSession().getAttribute("uid");
    if (o == null) throw new RuntimeException("Not authenticated");
    return (Integer) o;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getServletPath();

    try {
      if ("/dashboard".equals(path)) {
        int userId = uid(req);

        Preference prefs = prefDAO.get(userId);
        req.getSession().setAttribute("prefs", prefs);

        LocalDate now = LocalDate.now();

        int month = (req.getParameter("month") != null)
            ? Integer.parseInt(req.getParameter("month"))
            : now.getMonthValue();

        int year = (req.getParameter("year") != null)
            ? Integer.parseInt(req.getParameter("year"))
            : now.getYear();

        req.setAttribute("selectedMonth", month);
        req.setAttribute("selectedYear", year);

        List<Map<String, Object>> list = expenseDAO.listByMonth(userId, month, year);
        req.setAttribute("recentExpenses", list);

        double total = expenseDAO.monthTotal(userId, month, year);
        req.setAttribute("monthTotal", total);

        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
      }

      else if ("/expense/add".equals(path)) {
        int userId = uid(req);
        req.setAttribute("categories", categoryDAO.listForUser(userId));
        req.setAttribute("today", LocalDate.now().toString());
        req.setAttribute("selectedCat", req.getParameter("cat"));
        req.getRequestDispatcher("/add_expense.jsp").forward(req, resp);
      }

      else if ("/expense/edit".equals(path)) {
        int userId = uid(req);
        int id = Integer.parseInt(req.getParameter("id"));

        Map<String, Object> expense = expenseDAO.findById(userId, id);
        if (expense == null) {
          resp.sendRedirect(req.getContextPath() + "/dashboard");
          return;
        }

        LocalDate d = (LocalDate) expense.get("expenseDate");
        int month = (req.getParameter("month") != null)
            ? Integer.parseInt(req.getParameter("month"))
            : d.getMonthValue();
        int year = (req.getParameter("year") != null)
            ? Integer.parseInt(req.getParameter("year"))
            : d.getYear();

        req.setAttribute("selectedMonth", month);
        req.setAttribute("selectedYear", year);
        req.setAttribute("expense", expense);
        req.setAttribute("categories", categoryDAO.listForUser(userId));

        req.getRequestDispatcher("/edit_expense.jsp").forward(req, resp);
      }

      else if ("/expense/delete".equals(path)) {
        int userId = uid(req);
        int id = Integer.parseInt(req.getParameter("id"));
        expenseDAO.delete(userId, id);

        String month = req.getParameter("month");
        String year = req.getParameter("year");

        String redirect = req.getContextPath() + "/dashboard";
        if (month != null && year != null) {
          redirect += "?month=" + month + "&year=" + year;
        }
        resp.sendRedirect(redirect);
      }

      else if ("/expenses".equals(path)) {
        int userId = uid(req);
        req.setAttribute("expenses", expenseDAO.listAll(userId));
        req.getRequestDispatcher("/expenses.jsp").forward(req, resp);
      }

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String path = req.getServletPath();

    Preference prefs = (Preference) req.getSession().getAttribute("prefs");
    String currency = (prefs != null) ? prefs.getCurrency() : "USD";

    try {
      if ("/expense/add".equals(path)) {

        int userId = uid(req);

        String categoryRaw = req.getParameter("category_id");
        int categoryId;

        if ("custom".equals(categoryRaw)) {
          String customName = req.getParameter("custom_category_name");

          if (customName == null || customName.isBlank()) {
            req.setAttribute("error", "Please enter a custom category name.");
            doGet(req, resp);
            return;
          }

          categoryId = categoryDAO.createAndReturnId(userId, customName);
        } else {
          categoryId = Integer.parseInt(categoryRaw);
        }

        double amount = Double.parseDouble(req.getParameter("amount"));
        LocalDate date = LocalDate.parse(req.getParameter("expense_date"));

        String desc = req.getParameter("description");
        if (desc == null || desc.trim().isEmpty()) {
          desc = null;
        }

        String method = req.getParameter("payment_method");

        // stored procedure will also store currency
        expenseDAO.addViaProcedure(userId, categoryId, amount, date, desc, method, currency);

        resp.sendRedirect(req.getContextPath() + "/dashboard");
      }

      else if ("/expense/update".equals(path)) {

        int userId = uid(req);

        int expenseId = Integer.parseInt(req.getParameter("expense_id"));

        String categoryRaw = req.getParameter("category_id");
        int categoryId;

        if ("custom".equals(categoryRaw)) {
          String customName = req.getParameter("custom_category_name");

          if (customName == null || customName.isBlank()) {
            req.setAttribute("error", "Please enter a custom category name.");
            doGet(req, resp);
            return;
          }
          categoryId = categoryDAO.createAndReturnId(userId, customName);
        } else {
          categoryId = Integer.parseInt(categoryRaw);
        }

        double amount = Double.parseDouble(req.getParameter("amount"));
        LocalDate date = LocalDate.parse(req.getParameter("expense_date"));

        String desc = req.getParameter("description");
        if (desc == null || desc.trim().isEmpty()) {
          desc = null;
        }

        String method = req.getParameter("payment_method");

        expenseDAO.updateExpense(userId, expenseId, categoryId, amount, date, desc, method);

        String month = req.getParameter("month");
        String year = req.getParameter("year");

        String redirect = req.getContextPath() + "/dashboard";
        if (month != null && year != null) {
          redirect += "?month=" + month + "&year=" + year;
        }
        resp.sendRedirect(redirect);
      }

    } catch (Exception e) {
      req.setAttribute("error", e.getMessage());
      doGet(req, resp);
    }
  }
}
