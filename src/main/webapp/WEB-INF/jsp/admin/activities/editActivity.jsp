<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ page import="com.tracking.lang.Language" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <c:set var="lang" value="${sessionScope.lang.split('_')[0]}" />
    <title><fmt:message key="edit_activity" bundle="${myBundle}" /></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>

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
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/edit-act" %>?id=${param.id}&lang=en_EN">EN</a></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/edit-act" %>?id=${param.id}&lang=uk_UA">UA</a></li>
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

<section class="container">
    <div class="row">
        <div class="col-12 text-center"><h3><fmt:message key="edit_activity" bundle="${myBundle}" /></h3></div>
        <div class="col-2"></div>
        <form action="<%= request.getContextPath() + "/a/edit-act" %>" class="col-8 border rounded mt-2"
              method="post"
              enctype="multipart/form-data">
            <c:if test="${sessionScope.successMessage != null}">
                <div class="col-12 mt-2">
                    <div class="alert alert-success py-1"><c:out value="${sessionScope.successMessage}" /></div>
                </div>
            </c:if>
            <c:if test="${sessionScope.messageError != null}">
                <div class="col-12 mt-2">
                    <div class="alert alert-danger py-1"><c:out value="${sessionScope.messageError}" /></div>
                </div>
            </c:if>
            <div class="form-group mx-4">
                <label for="activityName" class="col-form-label-lg"><fmt:message key="activity_name" bundle="${myBundle}" /> <p class="d-inline-block mb-0"
                                                                                     style="color: red">*</p></label>
                <input type="text" class="form-control" id="activityName" name="activityName"
                       value="${sessionScope.activity.name}" placeholder="<fmt:message key="name" bundle="${myBundle}" />">
            </div>
            <div class="form-group mx-4">
                <label for="activityCategory" class="col-form-label-lg"><fmt:message key="category" bundle="${myBundle}" /></label>
                <div class="row row-cols-6 g-0">
                    <c:forEach var="category" items="${sessionScope.categoryList}">     <%-- localize --%>
                        <div class="form-check">
                            <c:choose>
                                <c:when test="${sessionScope.activity.categories.contains(category.id)}">
                                    <input type="checkbox" class="form-check-input" name="activityCategory"
                                           value="${category.id}"
                                           id="activityCategory" checked>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" class="form-check-input" name="activityCategory"
                                           value="${category.id}"
                                           id="activityCategory">
                                </c:otherwise>
                            </c:choose>
                            <label for="activityCategory" class="form-check-label">
                                <c:choose>
                                    <c:when test="${lang.equals(Language.EN.value)}">
                                        <c:out value="${category.nameEN}" />
                                    </c:when>
                                    <c:when test="${lang.equals(Language.UA.value)}">
                                        <c:out value="${category.nameUA}" />
                                    </c:when>
                                </c:choose>
                            </label>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="form-group mx-4">
                <label for="activityDescription" class="col-form-label-lg"><fmt:message key="description" bundle="${myBundle}" /> <p class="d-inline-block mb-0"
                                                                                          style="color: red">*</p>
                </label>
                <textarea class="form-control" id="activityDescription" name="activityDescription" rows="8"
                          placeholder="<fmt:message key="description" bundle="${myBundle}" />..."><c:out value="${sessionScope.activity.description}" /></textarea>
            </div>
            <div class="form-group mx-4">
                <label for="activityImage" class="col-form-label-lg"><fmt:message key="photo" bundle="${myBundle}" /></label>
                <input type="file" name="activityImage" id="activityImage" class="form-control"
                       accept=".jpg,.png,.svg">
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1"><fmt:message key="update" bundle="${myBundle}" /></button>
            </div>
        </form>
        <div class="col-2"></div>
    </div>
</section>

<%
    session.removeAttribute("successMessage");
    session.removeAttribute("messageError");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
