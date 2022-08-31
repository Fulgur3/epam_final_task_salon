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

    <fmt:message bundle="${locale}" key="locale.action.add.review" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.action.add.review" var="editReview"/>
    <fmt:message bundle="${locale}" key="locale.table.mark" var="markLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.message" var="messageLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.update" var="button"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <title>${pageTitle} | ${projectName}</title>
</head>
<body>
<c:if test="${empty sessionScope.user || empty sessionScope.review}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <h3>${editReview}:</h3>
            <form name="reviewEditForm" method="POST" action="app">
                <input type="hidden" name="command" value="updateReview"/>
                <input type="hidden" name="reviewId" value="${sessionScope.review.id}"/>
                <div class="form-group">
                    <label>${markLabel}:
                        <br/>
                        <input type="number"
                               value="${sessionScope.review.mark}"
                               name="reviewMark"
                               max="10"
                               min="1"
                               required/>
                    </label>
                </div>
                <div class="form-group">
                    <label>${messageLabel}:
                        <br/>
                        <textarea name="reviewMessage"
                                  maxlength="280" cols="20"
                                  rows="10"
                                  class="form-control noresize">${sessionScope.review.message}
                        </textarea>
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