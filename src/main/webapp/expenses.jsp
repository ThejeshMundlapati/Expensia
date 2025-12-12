<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp" %>

<h2>Your Expenses</h2>

<div class="card">
    <table class="table">
        <tr>
            <th>Date</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Description</th>
            <th>Pay Method</th>
            <th>Action</th>
        </tr>

        <c:forEach var="e" items="${expenses}">
            <tr>
                <td>${e.expenseDate}</td>
                <td>${e.categoryName}</td>
                <td>${e.amount}</td>
                <td>${e.description}</td>
                <td>${e.paymentMethod}</td>

                <td>
                    <c:url var="deleteUrl" value='/expense/delete'>
                        <c:param name="id" value="${e.expenseId}"/>
                    </c:url>
                    <a class="btn secondary" href="${deleteUrl}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<%@ include file="fragments/footer.jsp" %>
