package org.example.expensify.web;

import org.example.expensify.dao.AlertDAO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AlertNotificationFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    // No initialization needed
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {

    HttpServletRequest httpReq = (HttpServletRequest) request;
    HttpSession session = httpReq.getSession(false);

    // Only calculate for logged-in users (check if session exists and has uid)
    if (session != null && session.getAttribute("uid") != null) {
      try {
        int userId = (Integer) session.getAttribute("uid");  // Changed from "userId" to "uid"
        AlertDAO alertDAO = new AlertDAO();
        int unseenCount = alertDAO.countUnseen(userId);
        httpReq.setAttribute("unseenAlertsCount", unseenCount);
      } catch (Exception e) {
        // Log error but don't break the request
        e.printStackTrace();
        // Set to 0 if there's an error
        httpReq.setAttribute("unseenAlertsCount", 0);
      }
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    // No cleanup needed
  }
}