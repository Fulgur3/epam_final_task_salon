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
    <fmt:message bundle="${locale}" key="locale.page.title.login" var="pageTitle"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.full" var="projectNameFull"/>
    <fmt:message bundle="${locale}" key="locale.table.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.cardnumber" var="cardNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.message.authorizationfail" var="authFailMessage"/>
    <fmt:message bundle="${locale}" key="locale.message.noaccountyet" var="noAccountYet"/>
    <fmt:message bundle="${locale}" key="locale.message.forgotpassword" var="forgotPassword"/>
    <fmt:message bundle="${locale}" key="locale.action.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.action.signin" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.action.showpassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.message.userIsBanned" var="userIsBanned"/>
    <title>${pageTitle} | ${projectName}</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/inputScript.js"></script>
</head>
<body>
<main>
    <jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
    <div class="bg"></div>
    <c:if test="${not empty sessionScope.user}">
        <jsp:forward page="${pageContext.request.contextPath}/home"/>
    </c:if>
    <div class="container">
        <div class="row centered-form center-block">
            <div class="container col-lg-4 col-lg-offset-4">
                <c:if test="${sessionScope.userIsBanned}">
                    <div class="alert alert-danger">
                            ${userIsBanned}
                    </div>
                </c:if>
                <form name="loginForm" method="POST" action="app">
                    <input type="hidden" name="command" value="login"/>
                    <div class="form-group">
                        <label>${loginLabel}:
                            <br/>
                            <input type="text"
                                   name="login"
                                   maxlength="40"
                                   pattern="\w+{4, 40}"
                                   required/>
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${passwordLabel}:
                            <br/>
                            <input type="password" name="password" id="passwordField" maxlength="32" required/>
                        </label>
                        <br/>
                        <label><input type="checkbox" onclick="togglePasswordVisibility()">
                            ${showPassword}</label>
                    </div>
                    <button type="submit" class="btn btn-default">${signIn}</button>
                </form>
                <c:if test="${authFail == true}">
                    <div class="alert alert-danger alert-dismissible">
                            ${authFailMessage}
                    </div>
                </c:if>
                <p>${noAccountYet} <a href="${pageContext.request.contextPath}/register">${signUp}</a></p>
                <p><a href="${pageContext.request.contextPath}/resetPassword">${forgotPassword}</a></p>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
