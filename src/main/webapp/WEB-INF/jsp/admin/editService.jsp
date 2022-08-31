<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${locale}" key="locale.page.title.editservice" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.table.name" var="serviceNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.description" var="serviceDescriptionLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.price" var="servicePriceLabel"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>
    <fmt:message bundle="${locale}" key="locale.table.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.status.available" var="available"/>
    <fmt:message bundle="${locale}" key="locale.status.unavailable" var="unavailable"/>
    <fmt:message bundle="${locale}" key="locale.message.serviceexists" var="serviceExists"/>
    <fmt:message bundle="${locale}" key="locale.action.update" var="button"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${sessionScope.user.status ne 'admin' || empty sessionScope.activity}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <h3>${pageTitle}:</h3>
            <c:if test="${sessionScope.dataExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${serviceExists}</strong>
                </div>
            </c:if>
            <form name="activityEditForm" method="POST" action="app">
                <input type="hidden" name="command" value="updateActivity"/>
                <input type="hidden" name="activityId" value="${sessionScope.activity.id}"/>
                <div class="form-group">
                    <label>${serviceNameLabel}:
                        <br/>
                        <input type="text" name="activityName"
                               maxlength="40"
                               minlength="2"
                               value="${sessionScope.activity.name}"
                               pattern="[\p{L}\s]{2,40}" required/>
                    </label>
                </div>
                <div class="form-group">
                    <label>${serviceDescriptionLabel}:
                        <br/>
                        <textarea name="activityDescription"
                                  maxlength="280"
                                  cols="30"
                                  rows="10"
                                  class="form-control noresize"
                                  required>${sessionScope.activity.description}
                        </textarea>
                    </label>
                </div>
                <div class="form-group">
                    <label>${servicePriceLabel}:
                        <br/>
                        <input type="text"
                               name="activityPrice"
                               maxlength="10"
                               minlength="1"
                               pattern="[1-9]\d{0,5}\.?\d{0,2}"
                               value="${sessionScope.activity.price}"
                               required/> ${byn}
                    </label>
                </div>
                <div class="form-group">
                    <br/>
                    <label>${statusLabel}:
                        <select name="activityStatus">
                            <option value="available">${available}</option>
                            <option value="unavailable">${unavailable}</option>
                        </select>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
