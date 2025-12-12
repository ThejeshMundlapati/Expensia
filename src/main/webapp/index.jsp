<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Expensia - Login</title>

    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css' />">
</head>


<body class="login-bg">

<div class="login-wrapper">

    <div class="login-card">

        <h2>Login</h2>

        <c:if test="${not empty error}">
            <div class="flash error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <input class="input" name="username" placeholder="Username" required>
            <input class="input" type="password" name="password" placeholder="Password" required>
            <button class="btn" type="submit" style="width: 100%;">Login</button>
        </form>

        <div class="login-divider"></div>

        <h3>Register</h3>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <input class="input" name="username" placeholder="Username" required>
            <input class="input" type="password" name="password" placeholder="Password" required>
            <input class="input" name="name" placeholder="Full Name" required>
            <input class="input" type="email" name="email" placeholder="Email" required>
            <input class="input" name="phone" placeholder="Phone">
            <button class="btn" type="submit" style="width: 100%;">Create account</button>
        </form>

    </div>

</div>

</body>
</html>
