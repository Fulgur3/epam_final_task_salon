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
    <fmt:setBundle basename="cbb_info" var="cbb"/>

    <fmt:message bundle="${locale}" key="locale.action.resetpassword" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.table.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.resetpassword" var="button"/>
    <fmt:message bundle="${locale}" key="locale.message.emailnotfound" var="emailNotFoundMessage"/>
    <fmt:message bundle="${locale}" key="locale.message.newpasswordsent" var="resetPasswordSuccess"/>
    <fmt:message bundle="${cbb}" key="cbb.name.short" var="projectName"/>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${not empty sessionScope.user}">
    <jsp:forward page="/home.jsp"/>
</c:if>

<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <form name="resetPasswordForm" method="POST" action="app">
                <input type="hidden" name="command" value="resetPassword"/>
                <div class="form-group">
                    <label>${emailLabel}*:
                        <br/>
                        <input type="email"
                               name="email"
                               maxlength="50"
                               minlength="5"
                               pattern="([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})"
                               required/></label>
                    </label>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
            <c:if test="${noEmailFound == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${emailNotFoundMessage}</strong>
                </div>
            </c:if>
            <c:if test="${sessionScope.operationSuccess == true}">
                <div class="alert alert-success alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${resetPasswordSuccess}</strong>
                </div>
            </c:if>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>