<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp"%>
<body class="preference">

<h2 style="color: white;
           font-weight: 700;
           text-shadow:
               -1px -1px 3px rgba(0,0,0,0.8),
                1px -1px 3px rgba(0,0,0,0.8),
               -1px  1px 3px rgba(0,0,0,0.8),
                1px  1px 3px rgba(0,0,0,0.8);">Preferences</h2>

<form method="post" class="card form-container">

    <label>Theme Mode</label>
    <select class="input" name="theme_mode">
        <option value="LIGHT" ${pref.themeMode == 'LIGHT' ? 'selected' : ''}>Light</option>
        <option value="DARK" ${pref.themeMode == 'DARK' ? 'selected' : ''}>Dark</option>
    </select>

    <label>Currency</label>
    <select class="input" name="currency">
        <c:forEach var="cur" items="${['USD','INR','EUR','GBP','CAD','AUD']}">
            <option value="${cur}" ${pref.currency == cur ? 'selected' : ''}>${cur}</option>
        </c:forEach>
    </select>

    <label>Default Budget</label>
    <input class="input"
           type="number"
           step="0.01"
           min="0"
           name="default_budget"
           value="${budgetSettings.defaultBudget}"
           required>

    <button class="btn" type="submit">Save Preferences</button>

</form>
</body>

<%@ include file="fragments/footer.jsp"%>