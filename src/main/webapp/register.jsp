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
    <fmt:message bundle="${locale}" key="locale.page.title.registration" var="pageTitle"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.full" var="projectNameFull"/>
    <fmt:message bundle="${locale}" key="locale.table.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.password" var="passwordLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.cardnumber" var="cardNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.message.alreadyhaveaccount" var="haveAccountAlready"/>
    <fmt:message bundle="${locale}" key="locale.message.forgotpassword" var="forgotPassword"/>
    <fmt:message bundle="${locale}" key="locale.action.signin" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.action.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.action.showpassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.message.cardnumberexists" var="cardExistsMessage"/>
    <fmt:message bundle="${locale}" key="locale.message.emailexists" var="emailExistsMessage"/>
    <fmt:message bundle="${locale}" key="locale.message.loginexists" var="loginExistsMessage"/>
    <fmt:message bundle="${locale}" key="locale.message.phonenumberexists" var="phoneNumberExistsMessage"/>
    <title>${pageTitle} | ${projectName}</title>

    <script type="text/javascript" src="js/inputScript.js"></script>
</head>
<body>
<main>
    <jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
    <c:if test="${not empty sessionScope.user}">
        <jsp:forward page="/home.jsp"/>
    </c:if>

    <div class="container">
        <div class="row centered-form center-block">
            <c:if test="${loginExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${loginExistsMessage}</strong>
                </div>
            </c:if>
            <c:if test="${emailExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${emailExistsMessage}</strong>
                </div>
            </c:if>
            <c:if test="${phoneNumberExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${phoneNumberExistsMessage}</strong>
                </div>
            </c:if>
            <c:if test="${cardNumberExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${cardExistsMessage}</strong>
                </div>
            </c:if>
            <div class="container col-md-4 col-md-offset-4">
                <form name="registerForm" method="POST" action="app">
                    <input type="hidden" name="command" value="register"/>
                    <div class="form-group">
                        <label>${loginLabel}*:
                            <br/>
                            <input type="text"
                                   name="login"
                                   maxlength="40"
                                   pattern="\w+{4, 40}"
                                   data-toggle="tooltip"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.login"/>"
                                   placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.login"/>"
                                   required/>
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${passwordLabel}*:
                            <br/>
                            <input type="password"
                                   name="password"
                                   id="passwordField"
                                   maxlength="32"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.password"/>"
                                   required/>
                        </label>
                        <br/>
                        <label><input type="checkbox"
                                      onclick="togglePasswordVisibility()">
                            ${showPassword}
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${emailLabel}*:
                            <br/>
                            <input type="email"
                                   name="email"
                                   maxlength="50"
                                   minlength="5"
                                   pattern="([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5})"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.email"/>"
                                   placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.email"/>"
                                   required/></label>
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${phoneNumberLabel}*:
                            <br/>
                            <input type="text"
                                   name="phoneNumber"
                                   maxlength="13"
                                   minlength="13"
                                   pattern="\+(\d{12})"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.phonenumber"/>"
                                   placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.phonenumber"/>"
                                   required/>
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${userNameLabel}:<br/>
                            <input type="text"
                                   name="userName"
                                   maxlength="40"
                                   minlength="2"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.username"/>"
                                   placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.username"/>"
                                   pattern="[\p{L}\s]{2,40}"/>
                        </label>
                    </div>
                    <div class="form-group">
                        <label>${cardNumberLabel}*:<br/>
                            <input type="text"
                                   name="cardNumber"
                                   maxlength="16"
                                   minlength="16"
                                   pattern="\d{16}"
                                   title="<fmt:message bundle="${locale}" key="locale.requirement.cardnumber"/>"
                                   placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.cardnumber"/>"
                                   required/>
                        </label>
                    </div>

                    <button type="submit" class="btn btn-default">${signUp}</button>
                </form>
                <p>${haveAccountAlready} <a href="${pageContext.request.contextPath}/login">${signIn}</a></p>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
