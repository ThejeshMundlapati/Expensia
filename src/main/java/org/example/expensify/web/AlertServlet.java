package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.expensify.dao.AlertDAO;
import org.example.expensify.model.Alert;

@WebServlet(urlPatterns={"/alerts","/alerts/seen"})
public class AlertServlet extends HttpServlet {
  private final AlertDAO alertDAO = new AlertDAO();

  private int uid(HttpServletRequest req) {
    Object o = req.getSession().getAttribute("uid");
    if (o == null) throw new RuntimeException("Not authenticated");
    return (Integer)o;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      int userId = uid(req);
      List<Alert> list = alertDAO.list(userId, false);
      req.setAttribute("alerts", list);
      req.getRequestDispatcher("/alerts.jsp").forward(req, resp);
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      int userId = uid(req);
      int id = Integer.parseInt(req.getParameter("alert_id"));
      alertDAO.markSeen(userId, id);
      resp.sendRedirect(req.getContextPath()+"/alerts");
    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}