<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

<h1>Login</h1>

<div class="container">
    <div class="row">
        <div class="col-4"></div>
        <form action="<%= request.getContextPath() + "/login" %>" class="col-4 border rounded" method="post">
            <div class="form-group mx-4">
                <label for="email" class="col-form-label-lg">Email</label>
                <input type="text" class="form-control" id="email" name="email" value="${sessionScope.email}"
                       placeholder="Email" required>
                <c:if test="${sessionScope.emailError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.emailError}</div>
                    <% session.removeAttribute("emailError"); %>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="password" class="col-form-label-lg">Password</label>
                <input type="password" class="form-control" id="password" name="password" value=""
                       placeholder="Password" required>
                <c:if test="${sessionScope.passwordError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.passwordError}</div>
                    <% session.removeAttribute("passwordError"); %>
                </c:if>
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1">Sign In</button>
            </div>
            <div class="row form-group mx-4 my-3">
                <a class="btn btn-default" href="<%= request.getContextPath() + "/register" %>"
                   role="button">Register</a>
            </div>
        </form>
    </div>
</div>

<% session.removeAttribute("email"); %>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
