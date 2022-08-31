<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="${lang}">
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>
    <fmt:message bundle="${locale}" key="locale.page.title.userorders" var="pageTitle"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>

    <fmt:message bundle="${locale}" key="locale.table.id" var="idLabel"/>
    <fmt:message bundle="${locale}" key="locale.currency.byn" var="byn"/>
    <fmt:message bundle="${locale}" key="locale.table.time" var="timeLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.services" var="servicesLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.price" var="priceLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.paid" var="paidLabel"/>
    <fmt:message bundle="${locale}" key="locale.status.yes" var="yes"/>
    <fmt:message bundle="${locale}" key="locale.status.no" var="no"/>
    <fmt:message bundle="${locale}" key="locale.message.notenoughmoney" var="notEnoughMoneyMessage"/>
    <fmt:message bundle="${locale}" key="locale.action.cancel" var="cancelLabel"/>
    <fmt:message bundle="${locale}" key="locale.status.pending" var="pending"/>
    <fmt:message bundle="${locale}" key="locale.status.confirmed" var="confirmed"/>
    <fmt:message bundle="${locale}" key="locale.status.cancelled" var="cancelled"/>
    <fmt:message bundle="${locale}" key="locale.status.finished" var="finished"/>
    <fmt:message bundle="${locale}" key="locale.action.cancel" var="cancelButton"/>
    <fmt:message bundle="${locale}" key="locale.action.pay" var="payButton"/>

    <title>${pageTitle} | ${projectName}</title>
    <jsp:useBean id="now" class="java.util.Date"/>

    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${sessionScope.notEnoughMoney == true}">
    <div class="alert alert-danger alert-dismissible">
            ${notEnoughMoneyMessage}
    </div>
</c:if>
<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>${idLabel}</th>
            <th>${timeLabel}</th>
            <th>${statusLabel}</th>
            <th>${paidLabel}</th>
            <th>${servicesLabel}</th>
            <th>${priceLabel}</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="order" items="${orderList}">
            <tr>
                <td>${order.id}</td>
                <td><fmt:formatDate value="${order.dateTime}" type="both"
                                    dateStyle="short" timeStyle="short"/></td>
                <td>
                    <c:if test="${order.status eq 'pending'}">
                        ${pending}
                    </c:if>
                    <c:if test="${order.status eq 'cancelled'}">
                        ${cancelled}
                    </c:if>
                    <c:if test="${order.status eq 'confirmed'}">
                        ${confirmed}
                    </c:if>
                    <c:if test="${order.status eq 'finished'}">
                        ${finished}
                    </c:if>
                </td>
                <td>
                    <c:if test="${order.paid eq true}">
                        ${yes}
                    </c:if>
                    <c:if test="${order.paid eq false}">
                        ${no}
                    </c:if>
                </td>
                <td><c:forEach var="activity" items="${order.activityList}">
                    <a href="app?command=viewActivity&activityId=${activity.id}">
                            ${activity.name}
                    </a>
                    <br/>
                </c:forEach>
                </td>
                <td>${order.price} ${byn}</td>
                <c:if test="${order.status ne 'cancelled' && order.status ne 'finished' &&
                now < order.dateTime}">
                    <td>
                        <form name="cancelOrderForm" method="POST" action="app">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <input type="hidden" name="command" value="cancelOrder"/>
                            <input type="submit" class="btn btn-link" value="${cancelButton}"/>
                        </form>
                    </td>
                </c:if>
                <c:if test="${order.paid eq false && order.status ne 'cancelled' &&
                order.status ne 'finished' && now < order.dateTime}">
                    <td>
                        <form name="payForOrderForm" method="POST" action="app">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <input type="hidden" name="command" value="payForOrder"/>
                            <input type="submit" class="btn btn-link" value="${payButton}"/>
                        </form>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<ul class="pagination">
    <c:if test="${currentPage != 1}">
        <li><a href="app?command=vieUserOrders&pageNumber=${currentPage - 1}">&laquo;</a></li>
    </c:if>
    <c:forEach begin="1" end="${numberOfPages}" var="i">
        <c:choose>
            <c:when test="${currentPage eq i}">
                <li class="active"><a href="#">${i}</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="app?command=viewUserOrders&pageNumber=${i}">${i}</a></li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:if test="${currentPage lt numberOfPages}">
        <li><a href="app?command=viewUserOrders&pageNumber=${currentPage + 1}">&raquo;</a></li>
    </c:if>
</ul>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
