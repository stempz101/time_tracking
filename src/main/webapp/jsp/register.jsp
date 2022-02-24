<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <title>Document</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<h1>Registration</h1>

<div class="container">
    <div class="row">
        <div class="col-md-3 col-lg-4"></div>
        <form action="<%= request.getContextPath() + "/register" %>" class="col-lg-4 border rounded" method="post"
              enctype="multipart/form-data">
            <div class="form-group mx-4">
                <label for="last_name" class="col-form-label-lg">Last Name <p class="d-inline-block mb-0"
                                                                              style="color: red">*</p></label>
                <input type="text" class="form-control" id="last_name" name="last_name" value="${sessionScope.lastName}"
                       placeholder="Last Name" pattern="[A-Za-z'\s]+|[а-яА-ЯЇїІіЄєҐґ'\s]+" required>
                <c:if test="${sessionScope.lastNameError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.lastNameError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="first_name" class="col-form-label-lg">First Name <p class="d-inline-block mb-0"
                                                                                style="color: red">*</p></label>
                <input type="text" class="form-control" id="first_name" name="first_name"
                       value="${sessionScope.firstName}"
                       placeholder="First Name" pattern="[A-Za-z'\s]+|[а-яА-ЯЇїІіЄєҐґ'\s]+" required>
                <c:if test="${sessionScope.firstNameError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.firstNameError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="email" class="col-form-label-lg">Email <p class="d-inline-block mb-0" style="color: red">
                    *</p></label>
                <input type="text" class="form-control" id="email" name="email" value="${sessionScope.email}"
                       placeholder="Email" pattern="^[\w\-.]+@([\w-]+\.)+[\w-]{2,4}$" required>
                <c:if test="${sessionScope.emailError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.emailError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="password" class="col-form-label-lg">Password <p class="d-inline-block mb-0"
                                                                            style="color: red">*</p></label>
                <input type="password" class="form-control" id="password" name="password"
                       value="${sessionScope.password}"
                       placeholder="Password" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" required>
                <c:if test="${sessionScope.passwordError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.passwordError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="confirmPassword" class="col-form-label-lg">Confirm Password <p class="d-inline-block mb-0"
                                                                            style="color: red">*</p></label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                       placeholder="Confirm Password" pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$" required>
                <c:if test="${sessionScope.confirmError != null}">
                    <div class="alert alert-danger my-1 py-1">${sessionScope.confirmError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="image" class="col-form-label-lg">Photo</label>
                <input type="file" name="image" id="image" class="form-control"
                       accept=".jpg,.png,.svg">
<%--                <c:if test="${sessionScope.activityNameError != null ||--%>
<%--                                sessionScope.activityCategoriesError != null ||--%>
<%--                                sessionScope.activityDescriptionError != null}">--%>
<%--                    <div class="alert alert-primary mt-3 py-1"><b>(Optional)</b> Don't forget choose the preview</div>--%>
<%--                </c:if>--%>
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1">Register</button>
            </div>
            <div class="row form-group mx-4 my-3">
                <a class="btn btn-default" href="<%= request.getContextPath() + "/login" %>" role="button">Back to sign
                    in</a>
            </div>
            <c:if test="${sessionScope.regError != null}">
                <div class="alert alert-danger mb-3 py-1">${sessionScope.regError}</div>
            </c:if>
        </form>
    </div>
</div>

<%
    session.removeAttribute("lastName");
    session.removeAttribute("firstName");
    session.removeAttribute("email");
    session.removeAttribute("password");
    session.removeAttribute("lastNameError");
    session.removeAttribute("firstNameError");
    session.removeAttribute("emailError");
    session.removeAttribute("passwordError");
    session.removeAttribute("confirmError");
    session.removeAttribute("regError");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>