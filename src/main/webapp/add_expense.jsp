<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp"%>
<body class = "add_expense">
<h2 style="color: white;
           font-weight: 700;
           text-shadow:
               -1px -1px 3px rgba(0,0,0,0.8),
                1px -1px 3px rgba(0,0,0,0.8),
               -1px  1px 3px rgba(0,0,0,0.8),
                1px  1px 3px rgba(0,0,0,0.8);">Add Expense</h2>

<c:if test="${not empty error}">
    <div class="flash error">${error}</div>
</c:if>

<c:url var="saveUrl" value="/expense/add"/>
<form method="post" action="${saveUrl}" class="card form-container">
    <label>Amount</label>
    <input class="input" type="number" step="0.01" min="0" name="amount" required>

    <label>Date</label>
    <input class="input" type="date" name="expense_date" value="${today}" required>

    <label>Category</label>
    <select name="category_id" class="input" id="categorySelect" onchange="toggleCustomBox()">
        <c:forEach var="c" items="${categories}">
            <option value="${c.categoryId}">${c.categoryName}</option>
        </c:forEach>
        <option value="custom">+ Add Custom Category</option>
    </select>

    <div id="customCategoryBox" style="display:none; margin-top:10px;">
        <label>Enter Custom Category</label>
        <input class="input" name="custom_category_name" placeholder="e.g., Coffee">
    </div>

    <label>Payment Method</label>
    <select name="payment_method" class="input">
        <option>CARD</option>
        <option>CASH</option>
        <option>ONLINE</option>
        <option>BANK_TRANSFER</option>
    </select>

    <label>Description</label>
    <input class="input" name="description" placeholder="Optional">

    <button class="btn" type="submit">Save Expense</button>
</form>

<%@ include file="fragments/footer.jsp"%>
</body>
<script>
    function toggleCustomBox() {
        const sel = document.getElementById("categorySelect");
        const box = document.getElementById("customCategoryBox");
        box.style.display = sel.value === "custom" ? "block" : "none";
    }
</script>
