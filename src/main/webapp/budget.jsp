<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp"%>

<h2>Budget</h2>

<c:if test="${not empty success}">
    <div class="flash success">${success}</div>
</c:if>

<form method="post" class="card">
    <label>Month (1-12)</label>
    <input class="input" type="number" name="month" min="1" max="12" required>

    <label>Year</label>
    <input class="input" type="number" name="year" min="2000" max="2100" required>

    <label>Budget Limit</label>
    <input class="input" type="number" step="0.01" min="0" name="budget_limit" required>

    <button class="btn" type="submit">Save</button>
</form>

<%@ include file="fragments/footer.jsp"%>
