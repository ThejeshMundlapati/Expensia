<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Expensify - Register</title>
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">
</head>
<body>
<div class="container" style="max-width:480px">

    <h2>Create Your Account</h2>

    <c:if test="${not empty error}">
        <div class="flash error">${error}</div>
    </c:if>

    <form method="post" action="<c:url value='/register'/>">
        <label>Username</label>
        <input class="input" name="username" required>

        <label>Password</label>
        <input class="input" type="password" name="password" required>

        <label>Name</label>
        <input class="input" name="name" required>

        <label>Email</label>
        <input class="input" type="email" name="email" required>

        <label>Phone</label>
        <input class="input" name="phone">

        <button class="btn" type="submit">Register</button>
    </form>

    <p>Already have an account? <a href="<c:url value='/'/>">Login</a></p>

</div>
</body>
</html>

