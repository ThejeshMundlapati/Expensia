package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.expensify.dao.BudgetDAO;
import org.example.expensify.model.Budget;

@WebServlet(urlPatterns={"/budget"})
public class BudgetServlet extends HttpServlet {
  private final BudgetDAO budgetDAO = new BudgetDAO();

  private int uid(HttpServletRequest req) {
    Object o = req.getSession().getAttribute("uid");
    if (o == null) throw new RuntimeException("Not authenticated");
    return (Integer)o;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/budget.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      int userId = uid(req);
      int month = Integer.parseInt(req.getParameter("month"));
      int year  = Integer.parseInt(req.getParameter("year"));
      double limit = Double.parseDouble(req.getParameter("budget_limit"));
      Budget b = budgetDAO.upsert(userId, month, year, limit);
      req.setAttribute("success", "Budget saved for " + month + "/" + year);
      req.getRequestDispatcher("/budget.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}

