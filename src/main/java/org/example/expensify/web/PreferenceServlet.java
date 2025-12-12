package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.example.expensify.dao.BudgetSettingsDAO;
import org.example.expensify.dao.PreferenceDAO;
import org.example.expensify.model.BudgetSettings;
import org.example.expensify.model.Preference;

@WebServlet(urlPatterns = {"/preferences"})
public class PreferenceServlet extends HttpServlet {

  private final PreferenceDAO prefDAO = new PreferenceDAO();
  private final BudgetSettingsDAO budgetSettingsDAO = new BudgetSettingsDAO();

  private int uid(HttpServletRequest req) {
    Object o = req.getSession().getAttribute("uid");
    if (o == null) throw new RuntimeException("Not authenticated");
    return (int) o;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      int userId = uid(req);

      Preference pref = prefDAO.get(userId);
      BudgetSettings budgetSettings = budgetSettingsDAO.get(userId);

      req.getSession().setAttribute("prefs", pref);

      req.setAttribute("pref", pref);
      req.setAttribute("budgetSettings", budgetSettings);
      req.getRequestDispatcher("/preferences.jsp").forward(req, resp);

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      int userId = uid(req);

      // Get form values
      String theme = req.getParameter("theme_mode");
      String currency = req.getParameter("currency");
      double defaultBudget = Double.parseDouble(req.getParameter("default_budget"));

      // Update preferences (theme, currency)
      prefDAO.update(userId, theme, currency);

      // Update budget settings (default_budget only, keep alerts enabled with default threshold)
      budgetSettingsDAO.update(userId, defaultBudget, true, true, 10);

      // Refresh session
      Preference updated = prefDAO.get(userId);
      req.getSession().setAttribute("prefs", updated);

      resp.sendRedirect(req.getContextPath() + "/preferences");

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}