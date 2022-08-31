<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${locale}" key="locale.action.add.money" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.table.cardnumber" var="cardNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.money.add" var="moneyLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.add.money" var="button"/>
    <fmt:message bundle="${locale}" key="locale.message.cardnotfound" var="cardNotFoundMessage"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>
<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <form name="addMoneyForm" method="POST" action="app">
                <input type="hidden" name="command" value="addMoneyToCard"/>

                <div class="form-group">
                    <label>${cardNumberLabel}:<br/>
                        <input type="text"
                               name="cardNumber"
                               maxlength="16"
                               minlength="16"
                               pattern="\d{16}"
                               required/>
                    </label>
                </div>
                <div class="form-group">
                    <label>${moneyLabel}:<br/>
                        <input type="text"
                               name="money"
                               minlength="1"
                               maxlength="6"
                               pattern="[1-9]\d{0,5}\.?\d{0,2}"
                               title="<fmt:message bundle="${locale}" key="locale.requirement.price"/>"
                               placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.price"/>"
                               required/> ${byn}
                    </label>
                </div>
                <button type="submit" class="btn btn-default">${button}</button>
            </form>
            <c:if test="${noCardFound == true}">
                <div class="alert alert-danger alert-dismissible">
                        ${cardNotFoundMessage}
                </div>
            </c:if>
        </div>
    </div>
</div>
<footer>
    <jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</footer>
</body>
</html>
