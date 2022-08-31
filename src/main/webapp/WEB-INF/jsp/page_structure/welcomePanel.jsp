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

    <fmt:message bundle="${locale}" key="locale.message.welcome.short" var="welcomeMessage"/>
    <fmt:message bundle="${locale}" key="locale.status.guest" var="guest"/>
</head>
<body>
<div>
    <c:choose>
        <c:when test="${not empty sessionScope.user.userName}">
            <cbb:welcome name="${sessionScope.user.userName}" message="${welcomeMessage}"/>
        </c:when>
        <c:when test="${not empty sessionScope.user}">
            <cbb:welcome name="${sessionScope.user.login}" message="${welcomeMessage}"/>
        </c:when>
        <c:otherwise>
            <cbb:welcome name="${guest}" message="${welcomeMessage}"/>
        </c:otherwise>
    </c:choose>
    <br/>
    <br/>
</div>
</body>
</html>