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
    <fmt:message bundle="${locale}" key="locale.page.title.services" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.action.update" var="button"/>
    <fmt:message bundle="${locale}" key="locale.action.edit" var="edit"/>
    <fmt:message bundle="${locale}" key="locale.action.delete" var="delete"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>
    <fmt:message bundle="${locale}" key="locale.status.available" var="available"/>
    <fmt:message bundle="${locale}" key="locale.status.unavailable" var="unavailable"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <title>${activity.name} | ${projectName}</title>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>

<div class="row">
    <div class="col-lg-2"><br/></div>
    <div class="col-lg-4">
        <input type="hidden" name="activityId" value="${activity.id}"/>
        <h3 class="text-uppercase">${activity.name}</h3>
        <p class="text-muted">${activity.description}</p>
        <h3><strong>${activity.price} ${byn}</strong></h3>
        <c:if test="${activity.status eq 'available'}">
            <p class="bg-success text-lowercase">${available}</p>
        </c:if>
        <c:if test="${activity.status eq 'unavailable'}">
            <p class="bg-danger text-lowercase">${unavailable}</p>
        </c:if>
        <br/><br/>
        <c:if test="${sessionScope.user.status == 'admin' || sessionScope.user.status == 'user'}">
            <jsp:include page="/WEB-INF/jsp/user/addReview.jsp"/>
        </c:if>
    </div>
    <div class="col-lg-6">
        <c:forEach var="review" items="${reviewList}">
            <input type="hidden" name="reviewId" value="${review.id}"/>
            <div class="media">
                <div class="media-left">
                    <img src="image?userId=${review.userId}" class="media-object rounded"
                         style="width:60px"
                         onerror="this.style.display='none'">
                </div>
                <div class="media-body">
                    <div class="media-heading">
                        <h4>${review.userLogin}</h4>
                        <p class="text-muted">
                            <fmt:formatDate value="${review.creationDate}" type="both"
                                            dateStyle="short" timeStyle="short"/></p>
                        <h3 class="text-danger"><strong>${review.mark}</strong></h3>
                    </div>
                    <p>${review.message}</p>
                </div>
            </div>
            <br/>
            <c:if test="${sessionScope.user.status == 'admin'}">
                <form name="reviewDeleteForm" method="POST" action="app">
                    <input type="hidden" name="command" value="deleteReview"/>
                    <input type="hidden" name="reviewId" value="${review.id}"/>
                    <input type="submit" class="btn btn-link" value="${delete}"/>
                </form>
            </c:if> <c:if test="${sessionScope.user.id == review.userId}">
            <form name="reviewEditForm" method="POST" action="app">
                <input type="hidden" name="command" value="editReview"/>
                <input type="hidden" name="reviewId" value="${review.id}"/>
                <input type="submit" class="btn btn-link" value="${edit}"/>
            </form>
        </c:if>
        </c:forEach>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
