<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <fmt:setLocale value="${sessionScope.local}"/>
    <fmt:setBundle basename="locale.locale" var="locale"/>
    <fmt:setBundle basename="cbb_info" var="projectInfo"/>

    <fmt:message bundle="${projectInfo}" key="cbb.name.short" var="projectName"/>
    <fmt:message bundle="${locale}" key="locale.page.title.account" var="pageTitle"/>

    <fmt:message bundle="${locale}" key="locale.table.login" var="loginLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.oldpassword" var="oldPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.newpassword" var="newPasswordLabel"/>
    <fmt:message bundle="${locale}" key="locale.action.showpassword" var="showPassword"/>
    <fmt:message bundle="${locale}" key="locale.table.username" var="userNameLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.email" var="emailLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.phonenumber" var="phoneNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.cardnumber" var="cardNumberLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.money" var="moneyLabel"/>
    <fmt:message bundle="${locale}" key="locale.table.status" var="statusLabel"/>
    <fmt:message bundle="${locale}" key="locale.status.admin" var="admin"/>
    <fmt:message bundle="${locale}" key="locale.status.user" var="user"/>
    <fmt:message bundle="${locale}" key="locale.status.banneduser" var="banned"/>
    <fmt:message bundle="${locale}" key="locale.action.update" var="submit"/>
    <fmt:message bundle="${locale}" key="locale.currency.ua" var="UAH"/>

    <fmt:message bundle="${locale}" key="locale.message.authorizationfail" var="authFailMessage"/>

    <fmt:message bundle="${locale}" key="locale.action.update" var="button"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/inputScript.js"></script>

    <title>${pageTitle} | ${projectName}</title>

    <script>
        function validateFileUpload() {
            var MAX_SIZE = 16177215;
            var fuData = document.getElementById('fileChooser');
            var FileUploadPath = fuData.value;
            if (FileUploadPath == '') {
                alert("Please upload an image");
            } else {
                var Extension = FileUploadPath.substring(FileUploadPath.lastIndexOf('.') + 1).toLowerCase();
                if (Extension == "gif" || Extension == "png" || Extension == "bmp"
                    || Extension == "jpeg" || Extension == "jpg") {
                    if (fuData.files && fuData.files[0]) {
                        var size = fuData.files[0].size;
                        if (size > MAX_SIZE) {
                            alert("Maximum file size exceeds");
                            return;
                        } else {
                            var reader = new FileReader();
                            reader.onload = function (e) {
                                $('#blah').attr('src', e.target.result);
                            }
                            reader.readAsDataURL(fuData.files[0]);
                        }
                    }
                } else {
                    alert("Photo only allows file types of GIF, PNG, JPG, JPEG and BMP. ");
                }
            }
        }
    </script>
</head>
<body>

<jsp:include page="/WEB-INF/jsp/page_structure/header.jsp"/>
<c:if test="${empty sessionScope.user}">
    <jsp:forward page="${pageContext.request.contextPath}/login.jsp"/>
</c:if>

<div class="container">
    <c:if test="${authFail == true}">
        <div class="alert alert-danger alert-dismissible">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            <strong>${authFailMessage}</strong>
        </div>
    </c:if>
    <div class="col-lg-2"><br/></div>
    <div class="col-lg-4">
        <div class="container col-md-4 col-md-offset-6">
            <form name="registerForm" method="POST" action="app">
                <input type="hidden" name="command" value="updateUser"/>
                <div class="form-group">
                    <strong>${loginLabel}:</strong>
                    <br/>
                    ${sessionScope.user.login}
                </div>
                <div class="form-group">
                    <label>${oldPasswordLabel}*:
                        <br/>
                        <input type="password"
                               name="password"
                               id="passwordField"
                               maxlength="32"
                               minlength="6"
                               pattern="[\w^_]{6,32}"
                               required/></label>
                    <br/>
                    <label><input type="checkbox"
                                  onclick="togglePasswordVisibility()">
                        ${showPassword}
                    </label>
                </div>
                <div class="form-group">
                    <label>${newPasswordLabel}*:
                        <br/>
                        <input type="password"
                               name="newPassword"
                               id="newPasswordField"
                               maxlength="32"
                               minlength="6"
                               pattern="[\w^_]{6,32}"/></label>
                    <br/>
                    <label><input type="checkbox"
                                  onclick="toggleNewPasswordVisibility()">
                        ${showPassword}
                    </label>
                </div>
                <div class="form-group">
                    <strong>${emailLabel}:</strong>
                    <br/>
                    ${sessionScope.user.email}
                </div>
                <div class="form-group">
                    <strong>${phoneNumberLabel}:</strong>
                    <br/>
                    ${sessionScope.user.phoneNumber}
                </div>
                <div class="form-group">
                    <label>${userNameLabel}:<br/>
                        <input type="text"
                               name="userName"
                               value="${sessionScope.user.userName}"
                               maxlength="40"
                               minlength="2"
                               pattern="[\p{L}\s]{2,40}"/></label>
                </div>
                <div class="form-group">
                    <strong>${cardNumberLabel}:</strong>
                    <br/>
                    ${sessionScope.user.cardNumber}
                </div>
                <div class="form-group">
                    <strong>${moneyLabel}:</strong>
                    <br/>
                    ${money} ${byn}
                </div>

                <button type="submit" class="btn btn-default">${button}</button>
            </form>
        </div>
    </div>

    <div class="col-lg-1"><br/></div>
    <div class="col-lg-4">
        <img src="image?userId=${sessionScope.user.id}" height="200px"
             onerror="this.style.display='none'"/>
        <br/>
        <form name="updateUserForm" method="POST" action="image" enctype="multipart/form-data">
            <input type="hidden" name="userId" value="${sessionScope.user.id}"/>
            <input type="file" id="fileChooser" name="photo" size="50" required/><br><br>
            <input type="submit"
                   value="${submit}"
                   onsubmit="validateFileUpload()">
        </form>
    </div>
</div>
<div class="col-lg-1"><br/></div>
<footer>
    <jsp:include page="/WEB-INF/jsp/page_structure/footer.jsp"/>
</footer>
</body>
</html>
