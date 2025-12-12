package org.example.expensify.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.example.expensify.dao.ExpenseDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {

  private final ExpenseDAO expenseDAO = new ExpenseDAO();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.getRequestDispatcher("/export.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {
      int userId = (int) req.getSession().getAttribute("uid");

      LocalDate from = LocalDate.parse(req.getParameter("from_date"));
      String toRaw = req.getParameter("to_date");
      LocalDate to = (toRaw == null || toRaw.isBlank()) ? null : LocalDate.parse(toRaw);

      String format = req.getParameter("format");

      List<Map<String, Object>> rows = expenseDAO.listByDateRange(userId, from, to);

      if ("csv".equals(format)) {
        exportCSV(rows, from, to, resp);
      } else {
        exportPDF(rows, from, to, resp);
      }

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  private void exportCSV(List<Map<String, Object>> rows,
                         LocalDate from, LocalDate to,
                         HttpServletResponse resp) throws IOException {

    resp.setContentType("text/csv");
    resp.setHeader("Content-Disposition",
        "attachment; filename=expenses_" + from +
            (to != null ? "_to_" + to : "") + ".csv");

    PrintWriter out = resp.getWriter();
    out.println("Date,Amount,Category,Description,Payment,Currency");

    for (Map<String, Object> r : rows) {
      String desc = (String) r.get("description");
      if (desc == null) desc = "";

      desc = desc.replace("\"", "\"\"");

      out.println(
          r.get("expenseDate") + "," +
              r.get("amount") + "," +
              r.get("categoryName") + "," +
              "\"" + desc + "\"," +
              r.get("paymentMethod") + "," +
              r.get("currency")
      );
    }

    out.flush();
  }

  private String pdfEscape(String s) {
    if (s == null) return "";
    // Escape backslash and parentheses for PDF literals
    return s.replace("\\", "\\\\")
        .replace("(", "\\(")
        .replace(")", "\\)");
  }

  private void exportPDF(List<Map<String, Object>> rows,
                         LocalDate from, LocalDate to,
                         HttpServletResponse resp) throws IOException {

    resp.setContentType("application/pdf");
    resp.setHeader("Content-Disposition",
        "attachment; filename=expenses_" + from +
            (to != null ? "_to_" + to : "") + ".pdf");

    var out = resp.getOutputStream();

    out.println("%PDF-1.4");
    out.println("1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj");
    out.println("2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj");
    out.println("3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 600 800] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >> endobj");

    StringBuilder content = new StringBuilder();
    content.append("BT\n");
    content.append("/F1 10 Tf\n");
    content.append("50 780 Td\n");

    String title = "Expense Report " + from + (to != null ? " to " + to : "");
    content.append("(").append(pdfEscape(title)).append(") Tj\n");
    content.append("0 -18 Td\n");

    if (rows.isEmpty()) {
      content.append("(").append(pdfEscape("No expenses in this period.")).append(") Tj\n");
    } else {
      String header = String.format(
          "%-12s %-10s %-4s %-15s %-25s %-10s",
          "Date", "Amount", "Cur", "Category", "Description", "Payment"
      );
      content.append("(").append(pdfEscape(header)).append(") Tj\n");
      content.append("0 -14 Td\n");

      String sep = "---------------------------------------------------------------"
          + "------------------------------";
      content.append("(").append(pdfEscape(sep)).append(") Tj\n");
      content.append("0 -14 Td\n");

      for (Map<String, Object> r : rows) {
        String dateStr = String.valueOf(r.get("expenseDate"));

        double amt = ((Number) r.get("amount")).doubleValue();
        String amountStr = String.format("%.2f", amt);

        String cur = r.get("currency") != null ? r.get("currency").toString() : "";

        String category = r.get("categoryName") != null
            ? r.get("categoryName").toString()
            : "";

        String desc = r.get("description") != null
            ? r.get("description").toString()
            : "";

        String payment = r.get("paymentMethod") != null
            ? r.get("paymentMethod").toString()
            : "";

        if (desc.length() > 22) {
          desc = desc.substring(0, 22) + "...";
        }

        String line = String.format(
            "%-12s %-10s %-4s %-15s %-25s %-10s",
            dateStr, amountStr, cur, category, desc, payment
        );

        content.append("(").append(pdfEscape(line)).append(") Tj\n");
        content.append("0 -14 Td\n");
      }
    }

    content.append("ET");

    String stream = content.toString();

    out.println("4 0 obj << /Length " + stream.length() + " >> stream");
    out.println(stream);
    out.println("endstream endobj");

    out.println("5 0 obj << /Type /Font /Subtype /Type1 /Name /F1 /BaseFont /Courier >> endobj");

    out.println("xref 0 6");
    out.println("0000000000 65535 f ");
    out.println("trailer << /Size 6 /Root 1 0 R >>");
    out.println("startxref 999");
    out.println("%%EOF");

    out.flush();
  }
}
