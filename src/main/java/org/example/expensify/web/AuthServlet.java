package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.example.expensify.dao.PreferenceDAO;
import org.example.expensify.dao.UserDAO;
import org.example.expensify.model.Preference;
import org.example.expensify.model.User;

@WebServlet(urlPatterns={"/login","/register"})
public class AuthServlet extends HttpServlet {

  private final UserDAO userDAO = new UserDAO();
  private final PreferenceDAO prefDAO = new PreferenceDAO();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String path = req.getServletPath();

    try {
      if ("/login".equals(path)) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (userDAO.checkPassword(username, password)) {
          User u = userDAO.findByUsername(username);
          HttpSession session = req.getSession(true);
          session.setAttribute("uid", u.getUserId());
          session.setAttribute("name", u.getName());

          Preference p = prefDAO.get(u.getUserId());
          session.setAttribute("prefs", p);

          resp.sendRedirect(req.getContextPath() + "/dashboard");
          return;
        }

        req.setAttribute("error", "Invalid credentials");
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
      }

      else if ("/register".equals(path)) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        User u = userDAO.create(username, password, name, email, phone);

        HttpSession session = req.getSession(true);
        session.setAttribute("uid", u.getUserId());
        session.setAttribute("name", u.getName());

        Preference p = prefDAO.get(u.getUserId());
        session.setAttribute("prefs", p);

        resp.sendRedirect(req.getContextPath() + "/dashboard");
      }

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }
}
