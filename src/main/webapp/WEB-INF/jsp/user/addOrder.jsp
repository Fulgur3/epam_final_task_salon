<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${locale}" key="locale.page.title.addorder" var="pageTitle"/>
    <fmt:message bundle="${locale}" key="locale.table.cardnumber" var="cardNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.time" var="timeLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.services" var="servicesLabel"/>
    <fmt:message bundle="${locale}" key="locale.message.orderwarning" var="orderWarning"/>
    <fmt:message bundle="${locale}" key="locale.action.add" var="button"/>
    <fmt:message bundle="${projectInfo}" key="cbb.working.hours.start" var="minHours"/>
    <fmt:message bundle="${locale}" key="locale.message.orderexists" var="orderExistsMessage"/>
    <fmt:message bundle="${projectInfo}" key="cbb.working.hours.end" var="maxHours"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>
    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>

    <jsp:useBean id="now" class="java.util.Date"/>

    <title>${pageTitle} | ${projectName}</title>
    <script>
        var minTime = "${minHours}";

        function checkServices(){
            var checker = document.getElementsByName("activityId");
            var counter = 0;
            for (var i = 0; i < checker.length; i++){
                if (checker[i].checked){
                    document.getElementById("submitButton").disabled = false;
                } else {
                    counter++;
                }
            }
            if (counter == checker.length){
                document.getElementById("submitButton").disabled = true;
            }
        }

        function setMinTime() {
            console.log("Initial min time: " + minTime);
            var input = document.getElementById("dateInput").value;
            var dateEntered = new Date(input);
            console.log("input: " + input);
            console.log("converted to date: " + dateEntered);
            var now = new Date();
            console.log("time input: " + now);

            if (now.getDay() == dateEntered.getDay()) {
                var hours;
                var minutes;
                if (now.getHours() < 10) {
                    hours = "0" + now.getHours();
                } else {
                    hours = now.getHours();
                }
                if (now.getMinutes() < 10) {
                    minutes = "0" + now.getMinutes();
                } else {
                    minutes = now.getMinutes();
                }
                document.getElementById("timeInput").setAttribute("min", hours + ":" + minutes);
            } else {
                document.getElementById("timeInput").setAttribute("min", minTime);
            }
            console.log("min time is: " + document.getElementById("timeInput").getAttribute("min"));
        }
    </script>
</head>
<body>

<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/home.jsp"/>
</c:if>
<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>


<div class="container">
    <div class="row centered-form center-block">
        <div class="container col-md-4 col-md-offset-4">
            <c:if test="${sessionScope.orderExists == true}">
                <div class="alert alert-danger alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <strong>${orderExistsMessage}</strong>
                </div>
            </c:if>
            <c:if test="${sessionScope.illegalInput == true}">
                <div class="alert alert-danger">
                        ${orderWarning}
                </div>
            </c:if>
            <form name="addOrderForm" method="POST" action="app">
                <input type="hidden" name="command" value="viewOrder"/>
                <h3>${pageTitle}</h3>
                <div class="form-group">
                    <label>${timeLabel}:<br/>
                        <input type="date"
                               min="<fmt:formatDate value="${now}" pattern="yyyy-MM-dd" />"
                               name="orderDate"
                               title="<fmt:message bundle="${locale}" key="locale.requirement.orderdate"/>"
                               onchange="setMinTime()"
                               id="dateInput"
                               required/>
                        <input type="time"
                               name="orderTime"
                               id="timeInput"
                               title="<fmt:message bundle="${locale}" key="locale.requirement.ordertime"/>"
                               min="${minHours}"
                               max="${maxHours}"
                               required/>
                    </label>
                </div>
                ${servicesLabel}:
                <div class="form-group">
                    <c:forEach var="activity" items="${sessionScope.activityList}">
                        <label><input type="checkbox" name="activityId"
                                      value="${activity.id}"
                                      onchange="checkServices()">
                                ${activity.name} (${activity.price} ${UAH})<br></label>
                        <br/>
                    </c:forEach>
                </div>
                <button type="submit" id="submitButton"
                        class="btn btn-default" disabled>${button}</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</body>
</html>
