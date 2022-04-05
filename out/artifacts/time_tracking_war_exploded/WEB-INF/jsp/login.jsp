<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <title><fmt:message key="login" bundle="${myBundle}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

<div class="container">
    <div class="row">
        <div class="d-flex justify-content-center">
            <div class="text-center mt-3 px-1">
                <a href="<%= request.getContextPath() + "/login" %>?lang=en_EN" class="h5 text-decoration-none">EN</a>
            </div>
            <div class="text-center mt-3 px-1 border-start">
                <a href="<%= request.getContextPath() + "/login" %>?lang=uk_UA" class="h5 text-decoration-none">UA</a>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-4"></div>
        <form action="<%= request.getContextPath() + "/login" %>" class="col-4 border rounded mt-3" method="post">
            <div class="text-center mt-3">
                <h1><fmt:message key="login" bundle="${myBundle}" /></h1>
            </div>
            <c:if test="${sessionScope.messageError != null}">
                <div class="form-group mx-4 mt-4">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.messageError}</div>
                </div>
            </c:if>
            <div class="form-group mx-4">
                <label for="email" class="col-form-label-lg"><fmt:message key="email" bundle="${myBundle}" /></label>
                <input type="text" class="form-control" id="email" name="email" value="${sessionScope.email}"
                       placeholder="<fmt:message key="email" bundle="${myBundle}" />">
            </div>
            <div class="form-group mx-4">
                <label for="password" class="col-form-label-lg"><fmt:message key="password" bundle="${myBundle}" /></label>
                <input type="password" class="form-control" id="password" name="password" value=""
                       placeholder="<fmt:message key="password" bundle="${myBundle}" />">
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1"><fmt:message key="sign_in" bundle="${myBundle}" /></button>
            </div>
            <div class="row form-group mx-4 my-3">
                <a class="btn btn-default" href="<%= request.getContextPath() + "/register" %>"
                   role="button"><fmt:message key="register" bundle="${myBundle}" /></a>
            </div>
        </form>
    </div>
</div>

<%
    session.removeAttribute("email");
    session.removeAttribute("messageError");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
