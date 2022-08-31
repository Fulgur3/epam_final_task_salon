<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${locale}">
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

    <fmt:message bundle="${locale}" key="locale.action.signin" var="signIn"/>
    <fmt:message bundle="${locale}" key="locale.action.signup" var="signUp"/>
    <fmt:message bundle="${locale}" key="locale.page.title.home" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.message.welcome" var="introText"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.full" var="projectNameFull"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<main class="home">
    <div class="container">
        <h1 class="text-uppercase">${projectNameFull}</h1>
        <h4>
            <jsp:include page="/WEB-INF/jsp/page_structure/welcomePanel.jsp"/>
            ${introText}
        </h4>
        <br/>
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login" class="btn btn-default" style="
text-shadow: none;">${signIn}</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-danger" style="
text-shadow: none;">${signUp}</a>
        </c:if>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>