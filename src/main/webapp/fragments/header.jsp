<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    // Read preference object stored in session by PreferenceServlet
    org.example.expensify.model.Preference prefs =
            (org.example.expensify.model.Preference) session.getAttribute("prefs");

    String mode = "LIGHT";
    if (prefs != null && prefs.getThemeMode() != null) {
        mode = prefs.getThemeMode();
    }
%>

<!DOCTYPE html>
<html lang="en" data-theme="<%= mode %>">
<head>
    <meta charset="UTF-8">
    <title>Expensia</title>

    <!-- CSS -->
    <link rel="stylesheet" href="<c:url value='/assets/css/styles.css'/>">

    <meta name="viewport" content="width=device-width, initial-scale=1"/>
</head>

<body>
<header class="nav">
    <div class="brand">Expensia</div>

    <nav>
        <a href="<c:url value='/dashboard'/>">Dashboard</a>
        <a href="<c:url value='/expense/add'/>">Add Expense</a>
        <a href="<c:url value='/export'/>">Export</a>
        <a href="<c:url value='/preferences'/>">Preferences</a>
        <a href="<c:url value='/alerts'/>">Alerts</a>
        <a href="<c:url value='/logout'/>">Logout</a>
    </nav>
</header>

<main class="container">
