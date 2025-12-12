<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="fragments/header.jsp" %>
<body class = "export">
<h2 style="color: white;
           font-weight: 700;
           text-shadow:
               -1px -1px 3px rgba(0,0,0,0.8),
                1px -1px 3px rgba(0,0,0,0.8),
               -1px  1px 3px rgba(0,0,0,0.8),
                1px  1px 3px rgba(0,0,0,0.8);">Export Expenses</h2>

<div class="card" style="max-width:450px; margin-top:20px;">

    <form method="post" action="<c:url value='/export'/>">

        <label>From Date</label>
        <input class="input" type="date" name="from_date" required>

        <label style="margin-top:10px;">To Date </label>
        <input class="input" type="date" name="to_date">

        <label style="margin-top:10px;">Format</label>
        <select name="format" class="input">
            <option value="csv">CSV</option>
            <option value="pdf">PDF</option>
        </select>

        <button class="btn" style="margin-top:15px;">Export</button>
    </form>

</div>
</body>
<%@ include file="fragments/footer.jsp" %>
