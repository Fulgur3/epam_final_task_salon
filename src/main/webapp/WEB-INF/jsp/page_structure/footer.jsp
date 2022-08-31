<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="cbb" uri="cbbtaglib" %>

<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="${pageContext.request.contextPath}/js/support/jquery-3.3.1.min.js"></script>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${locale}" key="locale.contacts.email" var="email"/>
    <fmt:message bundle="${locale}" key="locale.contacts.github" var="github"/>
    <fmt:message bundle="${projectInfo}" key="cbb.contacts.email" var="emailLink"/>
    <fmt:message bundle="${projectInfo}" key="cbb.contacts.github" var="githubLink"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.full" var="fullName"/>
    <fmt:message bundle="${projectInfo}" key="cbb.year" var="year"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.2.0/css/all.css"
          integrity="sha384-hWVjflwFxL6sNzntih27bfxkr27PmbbK/iSvJ+a4+0owXq79v+lsFkW54bOGbiDQ"
          crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<footer>
    <p class="text-center text-muted text-lowercase">
        <span class="glyphicon glyphicon-envelope"></span> <a href="mailto:${emailLink}">${email}</a>
        <i class="fab fa-github"></i> <a href="${githubLink}" target="_blank">${github}</a>
        <br/>
    </p>
    <p class="text-center text-uppercase text-muted">
        <cbb:copyright projectName="${fullName}" year="${year}"/>
    </p>
</footer>
</body>
</html>