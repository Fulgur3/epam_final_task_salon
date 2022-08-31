<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${locale}" key="locale.page.title.addorder" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.table.time" var="timeLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.checkorder" var="checkOrder"/>
    <fmt:message bundle="${locale}" key="locale.table.price" var="priceLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.services" var="servicesLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.confirm" var="button"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>

    <title>${pageTitle} | ${projectName}</title>

</head>
<body>


<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <h3>${checkOrder}:</h3>
            <form name="orderCheckForm" method="POST" action="app">
                <input type="hidden" name="command" value="addOrder"/>
                <div class="form-group">
                    <label>${timeLabel}:
                        <br/>
                        <fmt:formatDate value="${sessionScope.order.dateTime}" type="both"
                                        dateStyle="short" timeStyle="short"/>
                    </label>
                </div>
                <div class="form-group">
                    <strong>${servicesLabel}:</strong>
                    <br/>
                    <c:forEach var="activity" items="${sessionScope.order.activityList}">
                        <input type="hidden" name="activityId" value="${activity.id}"/>
                        ${activity.name} (${activity.price} ${byn})<br/>
                    </c:forEach>
                </div>
                <div class="form-group">
                    <label>${priceLabel}:
                        <br/>
                        ${sessionScope.order.price} ${byn}
                    </label>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>