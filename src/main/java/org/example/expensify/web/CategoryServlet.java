package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.example.expensify.dao.CategoryDAO;

@WebServlet(urlPatterns={"/category/add"})
public class CategoryServlet extends HttpServlet {

  private final CategoryDAO categoryDAO = new CategoryDAO();

  private int uid(HttpServletRequest req) {
    Object o = req.getSession().getAttribute("uid");
    if (o == null) throw new RuntimeException("Not authenticated");
    return (Integer) o;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      int userId = uid(req);
      String name = req.getParameter("category_name");

      int newId = categoryDAO.createAndReturnId(userId, name);

      resp.sendRedirect(req.getContextPath() + "/expense/add?cat=" + newId);

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
