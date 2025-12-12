<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp"%>

<h2>Edit Expense</h2>

<c:if test="${not empty error}">
    <div class="flash error">${error}</div>
</c:if>

<c:url var="updateUrl" value="/expense/update"/>

<form method="post" action="${updateUrl}" class="card">

    <!-- hidden ids -->
    <input type="hidden" name="expense_id" value="${expense.expenseId}"/>
    <input type="hidden" name="month" value="${selectedMonth}"/>
    <input type="hidden" name="year" value="${selectedYear}"/>

    <label>Amount</label>
    <input class="input" type="number" step="0.01" min="0"
           name="amount" value="${expense.amount}" required>

    <label>Date</label>
    <input class="input" type="date"
           name="expense_date"
           value="${expense.expenseDate}" required>

    <label>Category</label>
    <select name="category_id" class="input" id="categorySelect" onchange="toggleCustomBox()">
        <c:forEach var="c" items="${categories}">
            <option value="${c.categoryId}"
                    <c:if test="${c.categoryId == expense.categoryId}">selected</c:if>>
                    ${c.categoryName}
            </option>
        </c:forEach>
        <option value="custom">+ Add Custom Category</option>
    </select>

    <div id="customCategoryBox" style="display:none; margin-top:10px;">
        <label>Enter Custom Category</label>
        <input class="input" name="custom_category_name" placeholder="e.g., Coffee">
    </div>

    <label>Payment Method</label>
    <select name="payment_method" class="input">
        <option value="CARD"  ${expense.paymentMethod == 'CARD'  ? 'selected' : ''}>CARD</option>
        <option value="CASH"  ${expense.paymentMethod == 'CASH'  ? 'selected' : ''}>CASH</option>
        <option value="ONLINE"${expense.paymentMethod == 'ONLINE'? 'selected' : ''}>ONLINE</option>
        <option value="BANK_TRANSFER"
        ${expense.paymentMethod == 'BANK_TRANSFER' ? 'selected' : ''}>BANK_TRANSFER</option>
    </select>

    <label>Description</label>
    <input class="input" name="description"
           value="${expense.description}" placeholder="Optional">

    <button class="btn" type="submit">Update Expense</button>
</form>

<%@ include file="fragments/footer.jsp"%>

<script>
    function toggleCustomBox() {
        const sel = document.getElementById("categorySelect");
        const box = document.getElementById("customCategoryBox");
        box.style.display = sel.value === "custom" ? "block" : "none";
    }
</script>
