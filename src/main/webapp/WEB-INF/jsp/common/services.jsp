<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>
    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.status.available" var="available"/>
    <fmt:message bundle="${locale}" key="locale.status.unavailable" var="unavailable"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<main>
    <jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
    <div class="container">
        <div class="row col-xs-offset-1">
            <%--<div class="panel-group">--%>
            <c:forEach var="activity" items="${activityList}">
                <div class="col-md-3">
                    <c:choose>
                        <c:when test="${activity.status == 'available'}">
                            <div class="panel panel-success">
                                <div class="panel-heading">
                                    <a href="app?command=viewActivity&activityId=${activity.id}">
                                            ${activity.name}
                                    </a>
                                    <p class="text-muted text-lowercase">${available}</p>
                                </div>
                                <div class="panel-body" style="background-image: url(${pageContext.request.contextPath}/images/card-bg-available.png);
                                        background-size: cover;">
                                        ${activity.description}
                                    <h4>${activity.price} ${byn}</h4>
                                </div>
                                <c:if test="${sessionScope.user.status == 'admin'}">
                                    <form name="activityListForm" method="POST" action="app">
                                        <input type="hidden" name="command" value="editActivity"/>
                                        <input type="hidden" name="activityId" value="${activity.id}"/>
                                        <div class="panel-footer">
                                            <input type="submit" class="btn btn-link" value="edit"/>
                                        </div>
                                    </form>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="panel panel-danger">
                                <div class="panel-heading">
                                    <a href="app?command=viewActivity&activityId=${activity.id}">
                                            ${activity.name}
                                    </a>
                                    <p class="text-muted text-lowercase">${unavailable}</p>
                                </div>
                                <div class="panel-body" style="background-image: url(${pageContext.request.contextPath}/images/card-bg-unavailable.png);
                                        background-size: cover;">
                                        ${activity.description}
                                    <h4>${activity.price} ${byn}</h4>
                                </div>
                                <c:if test="${sessionScope.user.status == 'admin'}">
                                    <form name="activityListForm" method="POST" action="app">
                                        <input type="hidden" name="command" value="editActivity"/>
                                        <input type="hidden" name="activityId" value="${activity.id}"/>
                                        <div class="panel-footer">
                                            <input type="submit" class="btn btn-link" value="edit"/>
                                        </div>
                                    </form>
                                </c:if>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:forEach>
            <%--</div>--%>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
