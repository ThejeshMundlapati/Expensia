package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.example.expensify.dao.PreferenceDAO;
import org.example.expensify.model.Preference;

@WebServlet(urlPatterns = {"/preferences"})
public class PreferenceServlet extends HttpServlet {

  private final PreferenceDAO prefDAO = new PreferenceDAO();

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

      // Load preference from DB
      Preference pref = prefDAO.get(userId);

      // Keep in session (used by header.jsp for theme + currency)
      req.getSession().setAttribute("prefs", pref);

      req.setAttribute("pref", pref);
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

      String theme = req.getParameter("theme_mode");
      String currency = req.getParameter("currency");
      double budget = Double.parseDouble(req.getParameter("default_budget"));

      // Update DB
      prefDAO.update(userId, theme, currency, budget);

      // Reload updated preferences
      Preference updated = prefDAO.get(userId);

      // Store in session for global use (header.jsp)
      req.getSession().setAttribute("prefs", updated);

      resp.sendRedirect(req.getContextPath() + "/preferences");

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
