<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
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

    <!-- Inline CSS for notification badge -->
    <style>
        .nav-item-with-badge {
            position: relative;
            display: inline-block;
        }

        .notification-badge {
            position: absolute;
            top: -8px;
            right: -12px;
            background-color: #ff3838;
            color: white;
            border-radius: 10px;
            padding: 2px 6px;
            font-size: 11px;
            font-weight: bold;
            min-width: 18px;
            text-align: center;
            box-shadow: 0 2px 4px rgba(0,0,0,0.3);
            animation: pulse 2s infinite;
            line-height: 1.2;
        }

        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.05);
            }
        }

        /* Adjust for dark theme */
        [data-theme="DARK"] .notification-badge {
            background-color: #ff5252;
            box-shadow: 0 2px 6px rgba(255,82,82,0.4);
        }
    </style>
</head>

<body>
<header class="nav">
    <div class="brand">Expensia</div>

    <nav>
        <a href="<c:url value='/dashboard'/>">Dashboard</a>
        <a href="<c:url value='/expense/add'/>">Add Expense</a>
        <a href="<c:url value='/export'/>">Export</a>
        <a href="<c:url value='/preferences'/>">Preferences</a>

        <!-- Alerts link with notification badge -->
        <span class="nav-item-with-badge">
            <a href="<c:url value='/alerts'/>">Alerts</a>
            <c:if test="${unseenAlertsCount > 0}">
                <span class="notification-badge">${unseenAlertsCount}</span>
            </c:if>
        </span>

        <a href="<c:url value='/logout'/>">Logout</a>
    </nav>
</header>

<main class="container">