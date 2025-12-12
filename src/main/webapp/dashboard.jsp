<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="fragments/header.jsp" %>
<body class="dashboard">
<h2 style="color: white;
           font-weight: 700;
           text-shadow:
               -1px -1px 3px rgba(0,0,0,0.8),
                1px -1px 3px rgba(0,0,0,0.8),
               -1px  1px 3px rgba(0,0,0,0.8),
                1px  1px 3px rgba(0,0,0,0.8);">Welcome, ${sessionScope.name}!</h2>

<div class="dashboard-layout">

    <div class="dashboard-sidebar">

        <div class="card dashboard-filter-card">
            <h3>Filter</h3>

            <form action="<c:url value='/dashboard'/>" method="get">

                <label>Month</label>
                <select name="month" class="input">
                    <c:forEach var="m" begin="1" end="12">
                        <option value="${m}" ${m == selectedMonth ? 'selected' : ''}>${m}</option>
                    </c:forEach>
                </select>

                <label>Year</label>
                <input type="number" name="year"
                       class="input"
                       value="${selectedYear}"
                       min="2000"
                       max="2100"/>

                <button class="btn" style="margin-top:12px;">Apply</button>
            </form>
        </div>


        <%
            Double totalObj = (Double) request.getAttribute("monthTotal");
            if (totalObj != null) {

                org.example.expensify.model.Preference p =
                        (org.example.expensify.model.Preference) session.getAttribute("prefs");

                String cur = (p != null && p.getCurrency() != null)
                        ? p.getCurrency()
                        : "USD";

                String sym;
                switch (cur) {
                    case "INR": sym = "₹"; break;
                    case "EUR": sym = "€"; break;
                    case "GBP": sym = "£"; break;
                    case "CAD": sym = "C$"; break;
                    case "AUD": sym = "A$"; break;
                    default:    sym = "$";
                }
        %>

        <div class="card dashboard-total-card">
            <h3>Monthly Total</h3>
            <p>
                <strong><%= sym %><%= totalObj %></strong>
                <span class="total-sub">for ${selectedMonth}/${selectedYear}</span>
            </p>
        </div>

        <% } %>

    </div>

    <div class="card dashboard-main">
        <h3>Expenses for ${selectedMonth} / ${selectedYear}</h3>

        <table class="table">
            <tr>
                <th>Date</th>
                <th>Amount</th>
                <th>Category</th>
                <th>Description</th>
                <th>Payment</th>
                <th>Action</th>
            </tr>

            <c:forEach var="e" items="${recentExpenses}">
            <tr>
                <td>${e.expenseDate}</td>

                <td>
                    <%
                        java.util.Map row = (java.util.Map) pageContext.findAttribute("e");
                        String curRow = (row.get("currency") != null)
                                ? row.get("currency").toString()
                                : "USD";

                        String symRow;
                        switch (curRow) {
                            case "INR": symRow = "₹"; break;
                            case "EUR": symRow = "€"; break;
                            case "GBP": symRow = "£"; break;
                            case "CAD": symRow = "C$"; break;
                            case "AUD": symRow = "A$"; break;
                            default:    symRow = "$";
                        }
                    %>
                    <%= symRow %>${e.amount}
                </td>

                <td>${e.categoryName}</td>
                <td><c:out value="${e.description}"/></td>
                <td>${e.paymentMethod}</td>

                <td class="actions">
                    <c:url var="editUrl" value="/expense/edit">
                        <c:param name="id" value="${e.expenseId}" />
                        <c:param name="month" value="${selectedMonth}" />
                        <c:param name="year" value="${selectedYear}" />
                    </c:url>

                    <c:url var="delUrl" value="/expense/delete">
                        <c:param name="id" value="${e.expenseId}" />
                        <c:param name="month" value="${selectedMonth}" />
                        <c:param name="year" value="${selectedYear}" />
                    </c:url>

                    <a class="btn secondary small" href="${editUrl}">Edit</a>
                    <a class="btn secondary small" href="${delUrl}">Delete</a>
                </td>

            </tr>
            </c:forEach>

        </table>

        <a class="btn" href="<c:url value='/expense/add'/>" style="margin-top:14px;">Add Expense</a>
    </div>

</div>
</body>

<%@ include file="fragments/footer.jsp" %>
