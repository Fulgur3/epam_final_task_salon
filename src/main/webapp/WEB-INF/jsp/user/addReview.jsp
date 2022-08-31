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
    <fmt:message bundle="${locale}" key="locale.action.add.review" var="addReview"/>
    <fmt:message bundle="${locale}" key="locale.table.mark" var="markLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.message" var="messageLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.add.review" var="button"/>
</head>
<body>
<h3>${addReview}:</h3>
<form name="addReviewForm" method="POST" action="app">
    <input type="hidden" name="activityId" value="${activity.id}"/>
    <input type="hidden" name="command" value="addReview"/>
    <div class="form-group">
        <label>${markLabel}:
            <br/>
            <input type="number"
                   name="reviewMark"
                   max="10"
                   min="1"
                   title="<fmt:message bundle="${locale}" key="locale.requirement.mark"/>"
                   required/>
        </label>
    </div>
    <div class="form-group">
        <label>${messageLabel}:
            <br/>
            <textarea name="reviewMessage"
                      maxlength="280" cols="20"
                      title="<fmt:message bundle="${locale}" key="locale.requirement.message"/>"
                      placeholder="<fmt:message bundle="${locale}" key="locale.placeholder.message"/>"
                      rows="10"
                      class="form-control noresize"></textarea>
        </label>
    </div>
    <button type="submit" class="btn btn-default">${button}</button>
</form>
</body>
</html>