<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

    <fmt:message bundle="${locale}" key="locale.action.changelanguage" var="chooseLanguage"/>
    <fmt:message bundle="${locale}" key="locale.status.english" var="english"/>
    <fmt:message bundle="${locale}" key="locale.status.ukrainian" var="ukrainian"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<form name="changeLangForm" method="POST" action="app">
    <input type="hidden" name="command" value="locale"/>
    <input type="hidden" name="page" value="${pageContext.request.requestURL}"/>
    <input type="hidden" name="query" value="${pageContext.request.queryString}"/>
    <button class="btn btn-link" type="submit" name="lang" value="ua">${ukrainian}</button>
    <button class="btn btn-link" type="submit" name="lang" value="en">${english}</button>
</form>
</body>
</html>