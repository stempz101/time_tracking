<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ page import="com.tracking.models.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title><fmt:message key="title.edit_password" bundle="${myBundle}" /></title>
</head>
<body>

<c:choose>
    <c:when test="${sessionScope.authUser.admin}">
        <header class="p-3 mb-3 border-bottom">
            <div class="container">
                <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                    <a href="<%= request.getContextPath() + "/a/activities" %>" class="d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none">
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="32" class="me-2" viewBox="0 0 118 94" role="img"><title>Bootstrap</title><path fill-rule="evenodd" clip-rule="evenodd" d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z" fill="currentColor"></path></svg>
                    </a>

                    <div class="me-lg-auto"></div>
                    <ul class="nav col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                        <li>
                            <a href="#" class="nav-link px-2 link-secondary" id="dropdownActivities" data-bs-toggle="dropdown" aria-expanded="false"><fmt:message key="activities" bundle="${myBundle}" /></a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownActivities">
                                <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item"><fmt:message key="show" bundle="${myBundle}" /></a></li>
                                <li><a href="<%= request.getContextPath() + "/a/add-act" %>" class="dropdown-item"><fmt:message key="add" bundle="${myBundle}" /></a></li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" class="nav-link px-2 link-dark" id="dropdownCategories" data-bs-toggle="dropdown" aria-expanded="false"><fmt:message key="categories" bundle="${myBundle}" /></a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownCategories">
                                <li><a href="<%= request.getContextPath() + "/a/categories" %>" class="dropdown-item"><fmt:message key="show" bundle="${myBundle}" /></a></li>
                                <li><a href="<%= request.getContextPath() + "/a/add-cat" %>" class="dropdown-item"><fmt:message key="add" bundle="${myBundle}" /></a></li>
                            </ul>
                        </li>
                        <li>
                            <a href="#" class="nav-link px-2 link-dark" id="dropdownUsers" data-bs-toggle="dropdown" aria-expanded="false"><fmt:message key="users" bundle="${myBundle}" /></a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownUsers">
                                <li><a href="<%= request.getContextPath() + "/a/users" %>" class="dropdown-item"><fmt:message key="show" bundle="${myBundle}" /></a></li>
                                <li><a href="<%= request.getContextPath() + "/a/new-adm" %>" class="dropdown-item"><fmt:message key="new_admin" bundle="${myBundle}" /></a></li>
                            </ul>
                        </li>
                        <li><a href="<%= request.getContextPath() + "/a/requests" %>" class="nav-link px-2 link-dark"><fmt:message key="requests" bundle="${myBundle}" /></a></li>
                        <li>
                            <a href="#" class="nav-link px-2 link-dark" id="dropdownLang" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${sessionScope.lang.equals('en_EN')}">
                                        <p class="ms-1 me-1 mb-0">EN</p>
                                    </c:when>
                                    <c:when test="${sessionScope.lang.equals('uk_UA')}">
                                        <p class="ms-1 me-1 mb-0">UA</p>
                                    </c:when>
                                </c:choose>
                            </a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownLang">
                                <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/edit-pass" %>?lang=en_EN">EN</a></li>
                                <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/edit-pass" %>?lang=uk_UA">UA</a></li>
                            </ul>
                        </li>
                    </ul>

                    <div class="dropdown text-end ps-4 border-start">
                        <a href="#" class="d-block link-dark text-decoration-none" id="dropdownUser1" data-bs-toggle="dropdown" aria-expanded="false">
                            <div class="d-flex align-items-center dropdown-toggle">
                                <c:choose>
                                    <c:when test="${sessionScope.authUser.image == null}">
                                        <div class="rounded-circle me-1" style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_DEFAULT_IMG%>);background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="rounded-circle me-1" style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_IMG_UPLOAD_DIRECTORY%>${sessionScope.authUser.image});background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                    </c:otherwise>
                                </c:choose>
                                <p class="ms-1 me-1 mb-0"><c:out value="${sessionScope.authUser.lastName} ${sessionScope.authUser.firstName}"/></p>
                            </div>
                                <%--                    <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" class="rounded-circle me-1">--%>
                                <%--                    <c:out value="${sessionScope.authUser.lastName} ${sessionScope.authUser.firstName}" />--%>
                        </a>
                        <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                            <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/profile" %>?id=${sessionScope.authUser.id}"><fmt:message key="profile" bundle="${myBundle}" /></a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="<%= request.getContextPath() + "/logout" %>"><fmt:message key="sign_out" bundle="${myBundle}" /></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </header>
    </c:when>
    <c:otherwise>
        <header class="p-3 mb-3 border-bottom">
            <div class="container">
                <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                    <a href="<%= request.getContextPath() + "/u/activities" %>"
                       class="d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none">
                        <svg xmlns="http://www.w3.org/2000/svg" width="40" height="32" class="me-2" viewBox="0 0 118 94"
                             role="img"><title>Bootstrap</title>
                            <path fill-rule="evenodd" clip-rule="evenodd"
                                  d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"
                                  fill="currentColor"></path>
                        </svg>
                    </a>

                    <div class="me-lg-auto"></div>
                    <ul class="nav col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                        <li>
                            <a href="#" class="nav-link px-2 link-secondary" id="dropdownActivities2" data-bs-toggle="dropdown"
                               aria-expanded="false"><fmt:message key="activities" bundle="${myBundle}" /></a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownActivities2">
                                    <%--                        <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item">Show</a></li>--%>
                                    <%--                        <li><a href="<%= request.getContextPath() + "/a/add-act" %>" class="dropdown-item">Add</a></li>--%>
                                <li><a href="<%= request.getContextPath() + "/u/activities" %>" class="dropdown-item"><fmt:message key="show" bundle="${myBundle}" /></a>
                                </li>
                                <li><a href="<%= request.getContextPath() + "/u/add-act" %>" class="dropdown-item"><fmt:message key="add" bundle="${myBundle}" /></a></li>
                            </ul>
                        </li>
                            <%--                <li><a href="<%= request.getContextPath() + "/a/requests" %>" class="nav-link px-2 link-dark">Requests</a></li>--%>
                        <li><a href="<%= request.getContextPath() + "/u/requests" %>" class="nav-link px-2 link-dark"><fmt:message key="requests" bundle="${myBundle}" /></a></li>
                        <li>
                            <a href="#" class="nav-link px-2 link-dark" id="dropdownLang2" data-bs-toggle="dropdown" aria-expanded="false">
                                <c:choose>
                                    <c:when test="${sessionScope.lang.equals('en_EN')}">
                                        <p class="ms-1 me-1 mb-0">EN</p>
                                    </c:when>
                                    <c:when test="${sessionScope.lang.equals('uk_UA')}">
                                        <p class="ms-1 me-1 mb-0">UA</p>
                                    </c:when>
                                </c:choose>
                            </a>
                            <ul class="dropdown-menu text-small" aria-labelledby="dropdownLang2">
                                <li><a class="dropdown-item" href="<%= request.getContextPath() + "/u/edit-pass" %>?lang=en_EN">EN</a></li>
                                <li><a class="dropdown-item" href="<%= request.getContextPath() + "/u/edit-pass" %>?lang=uk_UA">UA</a></li>
                            </ul>
                        </li>
                    </ul>

                    <div class="dropdown text-end ps-4 border-start">
                        <a href="#" class="d-block link-dark text-decoration-none" id="dropdownUser2" data-bs-toggle="dropdown"
                           aria-expanded="false">
                            <div class="d-flex align-items-center dropdown-toggle">
                                <c:choose>
                                    <c:when test="${sessionScope.authUser.image == null}">
                                        <div class="rounded-circle me-1"
                                             style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_DEFAULT_IMG%>);background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="rounded-circle me-1"
                                             style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_IMG_UPLOAD_DIRECTORY%>${sessionScope.authUser.image});
                                                     background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                    </c:otherwise>
                                </c:choose>
                                <p class="ms-1 me-1 mb-0"><c:out
                                        value="${sessionScope.authUser.lastName} ${sessionScope.authUser.firstName}"/></p>
                            </div>
                                <%--                    <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" class="rounded-circle me-1">--%>
                                <%--                    <c:out value="${sessionScope.authUser.lastName} ${sessionScope.authUser.firstName}" />--%>
                        </a>
                        <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser2">
                            <li><a class="dropdown-item" href="<%= request.getContextPath() + "/u/profile" %>?id=${sessionScope.authUser.id}"><fmt:message key="profile" bundle="${myBundle}" /></a></li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li><a class="dropdown-item" href="<%= request.getContextPath() + "/logout" %>"><fmt:message key="sign_out" bundle="${myBundle}" /></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </header>
    </c:otherwise>
</c:choose>

<section class="container">
    <div class="row">
        <div class="col-md-3 col-lg-4"></div>
        <%
            User authUser = (User) session.getAttribute("authUser");
            String role = authUser.isAdmin() ? "/a" : "/u";
        %>
        <form action="<%= request.getContextPath() + role + "/edit-pass"%>" class="col-lg-4 border rounded" method="post">
            <c:if test="${sessionScope.messageError != null}">
                <div class="form-group mx-4 mt-4">
                    <div class="alert alert-danger my-1 py-1"><c:out value="${sessionScope.messageError}" /></div>
                </div>
            </c:if>
            <div class="form-group mx-4">
                <label for="currentPassword" class="col-form-label-lg"><fmt:message key="current_password" bundle="${myBundle}" /></label>
                <input type="password" class="form-control" id="currentPassword" name="currentPassword" value=""
                       placeholder="<fmt:message key="current_password" bundle="${myBundle}" />">
            </div>
            <div class="form-group mx-4">
                <label for="newPassword" class="col-form-label-lg"><fmt:message key="new_password" bundle="${myBundle}" /></label>
                <input type="password" class="form-control" id="newPassword" name="newPassword" value=""
                       placeholder="<fmt:message key="new_password" bundle="${myBundle}" />">
            </div>
            <div class="form-group mx-4">
                <label for="confirmPassword" class="col-form-label-lg"><fmt:message key="confirm_password" bundle="${myBundle}" /></label>
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" value=""
                       placeholder="<fmt:message key="new_password" bundle="${myBundle}" />">
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1"><fmt:message key="update" bundle="${myBundle}" /></button>
            </div>
        </form>
    </div>
</section>

<%
    session.removeAttribute("messageError");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
