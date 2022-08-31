<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${locale}" key="locale.page.title.users" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.table.id" var="idLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.status.admin" var="adminRole"/>
    <fmt:message bundle="${locale}" key="locale.status.user" var="userRole"/>
    <fmt:message bundle="${locale}" key="locale.status.banneduser" var="bannedRole"/>
    <fmt:message bundle="${locale}" key="locale.action.changestatus" var="changeStatus"/>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${sessionScope.user.status ne 'admin'}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>


<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>${idLabel}</th>
            <th>${loginLabel}</th>
            <th>${userNameLabel}</th>
            <th>${statusLabel}</th>
            <th>${emailLabel}</th>
            <th>${phoneNumberLabel}</th>
            <th>${changeStatus}</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${userList}">
            <tr>
                <form name="loginForm" method="POST" action="app">
                    <input type="hidden" name="command" value="changeUserStatus"/>
                    <input type="hidden" name="userId" value="${user.id}"/>
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.userName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${user.status == 'admin'}">
                                ${adminRole}
                            </c:when>
                            <c:when test="${user.status == 'banned'}">
                                ${bannedRole}
                            </c:when>
                            <c:otherwise>
                                ${userRole}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${user.email}</td>
                    <td>${user.phoneNumber}</td>
                    <td>
                        <select name="userStatus">
                            <option value="user">${userRole}</option>
                            <option value="admin">${adminRole}</option>
                            <option value="banned">${bannedRole}</option>
                        </select>
                        <input type="submit" value="${changeStatus}"></td>
                </form>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
