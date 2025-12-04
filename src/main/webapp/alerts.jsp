<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp"%>
<body class = "alert">
<h2 style="color: white;
           font-weight: 700;
           text-shadow:
               -1px -1px 3px rgba(0,0,0,0.8),
                1px -1px 3px rgba(0,0,0,0.8),
               -1px  1px 3px rgba(0,0,0,0.8),
                1px  1px 3px rgba(0,0,0,0.8);">Alerts</h2>

<div class="card">
    <table class="table">
        <tr>
            <th>When</th>
            <th>Type</th>
            <th>Message</th>
            <th>Seen</th>
            <th>Action</th>
        </tr>

        <c:forEach var="a" items="${alerts}">
            <tr>
                <td>${a.month}/${a.year}</td>
                <td>${a.alertType}</td>
                <td><c:out value="${a.message}"/></td>
                <td>${a.seen}</td>

                <td>
                    <c:url var="seenUrl" value='/alerts/seen'/>
                    <form method="post" action="${seenUrl}">
                        <input type="hidden" name="alert_id" value="${a.alertId}">
                        <button class="btn secondary" type="submit">Mark seen</button>
                    </form>
                </td>

            </tr>
        </c:forEach>
    </table>
</div>
</body>

<%@ include file="fragments/footer.jsp"%>
