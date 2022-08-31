<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>

    <fmt:message bundle="${locale}" key="locale.page.title.home" var="homePage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.services" var="servicesPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.login" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.page.title.registration" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.page.title.userorders" var="myOrders"/>
    <fmt:message bundle="${locale}" key="locale.page.title.allorders" var="allOrders"/>
    <fmt:message bundle="${locale}" key="locale.page.title.account" var="accountPage"/>
    <fmt:message bundle="${locale}" key="locale.page.title.addmoney" var="addMoney"/>
    <fmt:message bundle="${locale}" key="locale.page.title.addservice" var="addService"/>
    <fmt:message bundle="${locale}" key="locale.page.title.addorder" var="addOrder"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="usersPage"/>
    <fmt:message bundle="${locale}" key="locale.action.logout" var="logout"/>
    <fmt:message bundle="${locale}" key="locale.action.control" var="control"/>

</head>
<body>

<nav class="navbar navbar-inverse navbar-fixed-top text-uppercase">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li>
                <a href="${pageContext.request.contextPath}/home.jsp">${homePage}</a>
            </li>
            <li>
                <a href="app?command=viewActivities">${servicesPage}</a>
            </li>

            <c:if test="${empty sessionScope.user}">
            <li><a href="${pageContext.request.contextPath}/register.jsp">${signUp}</a></li>
            <li><a href="${pageContext.request.contextPath}/login.jsp">${signIn}</a></li>
            </c:if>
            <c:if test="${sessionScope.user ne null && sessionScope.user.status ne 'banned'}">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">${accountPage}
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="app?command=viewUserInfo">${accountPage}</a></li>
                    <li><a href="app?command=viewUserOrders&pageNumber=1">${myOrders}</a></li>
                    <li><a href="${pageContext.request.contextPath}/addMoney">${addMoney}</a></li>
                </ul>
            </li>
            </c:if>
            <c:if test="${sessionScope.user ne null && sessionScope.user.status == 'admin'}">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="#">${control}
                    <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="${pageContext.request.contextPath}/addService">${addService}</a></li>
                    <li><a href="app?command=viewUsers">${usersPage}</a></li>
                    <li><a href="app?command=viewAllOrders&pageNumber=1">${allOrders}</a></li>
                </ul>
            </li>
            </c:if>
            <c:if test="${sessionScope.user ne null && sessionScope.user.status ne 'banned'}">
            <li><a href="app?command=createOrder">${addOrder}</a></li>
            </c:if>
            <ul class="nav navbar-nav navbar-right text-uppercase">
                <c:if test="${sessionScope.user ne null}">
                    <li><a href="app?command=logout">${logout}</a></li>
                </c:if>
                <li><jsp:include page="/WEB-INF/jsp/page_structure/chooseLang.jsp"/></li>
            </ul>

    </div>
</nav>
</body>
</html>
