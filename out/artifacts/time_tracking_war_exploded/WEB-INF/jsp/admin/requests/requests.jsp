<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tg" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title><fmt:message key="requests" bundle="${myBundle}" /></title>
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
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/requests" %>?lang=en_EN">EN</a></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/requests" %>?lang=uk_UA">UA</a></li>
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

<div class="container">
    <div class="row mb-2">
        <div class="col-12 text-center"><h1><fmt:message key="requests" bundle="${myBundle}" /></h1></div>
        <c:if test="${sessionScope.successMessage != null}">
            <div class="col-12 mt-2">
                <div class="alert alert-success py-1"><c:out value="${sessionScope.successMessage}" /></div>
            </div>
        </c:if>
        <div class="row mt-2">
            <div class="d-inline-flex justify-content-between align-items-center">
                <div class="d-flex"></div>
                <div class="d-flex">
                    <form action="<%= request.getContextPath() + "/a/requests" %>"
                          class="d-flex col-12 col-lg-auto mx-0 mb-2 justify-content-center align-items-center"
                          method="get">
                        <div class="d-flex">
                            <a href="<%= request.getContextPath() + "/a/requests" %>" class="btn btn-primary"><fmt:message key="reset" bundle="${myBundle}" /></a>
                            <select class="form-select ms-2" aria-label="Sort by" name="sort" id="sort">
                                <option value=""><fmt:message key="sort_by" bundle="${myBundle}" /></option>
                                <c:choose>
                                    <c:when test="${param.sort.equals('status')}">
                                        <option value="status" selected><fmt:message key="sort.status" bundle="${myBundle}" /></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="status"><fmt:message key="sort.status" bundle="${myBundle}" /></option>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${param.sort.equals('create_time')}">
                                        <option value="create_time" selected><fmt:message key="sort.time" bundle="${myBundle}" /></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="create_time"><fmt:message key="sort.time" bundle="${myBundle}" /></option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                            <select class="form-select ms-2" aria-label="Order by" name="order" id="orderBy"
                                    required>
                                <c:choose>
                                    <c:when test="${param.order.equals('asc') ||
                                                param.order == null ||
                                                empty param.order}">
                                        <option value="asc" selected><fmt:message key="ascending" bundle="${myBundle}" /></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="asc"><fmt:message key="ascending" bundle="${myBundle}" /></option>
                                    </c:otherwise>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${param.order.equals('desc')}">
                                        <option value="desc" selected><fmt:message key="descending" bundle="${myBundle}" /></option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="desc"><fmt:message key="descending" bundle="${myBundle}" /></option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                            <button type="submit" class="btn btn-primary" style="width: 100px; margin-left: 11px;"
                                    value="1">
                                <fmt:message key="sort" bundle="${myBundle}" />
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            <div class="">
                <table class="table text-center align-middle table-bordered table-striped mt-2">
                    <thead>
                    <tr>
                        <th><fmt:message key="creator" bundle="${myBundle}" /></th>
                        <th><fmt:message key="activity" bundle="${myBundle}" /></th>
                        <th><fmt:message key="status" bundle="${myBundle}" /></th>
                        <th><fmt:message key="requests.add" bundle="${myBundle}" />/<fmt:message key="requests.remove" bundle="${myBundle}" /></th>
                        <th><fmt:message key="time" bundle="${myBundle}" /></th>
                        <th class="w-25"><fmt:message key="action" bundle="${myBundle}" /></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="request" items="${requestScope.requestList}">
                        <c:choose>
                            <c:when test="${request.status.value.equals('CONFIRMED')}">
                                <tr class="table-success">
                                    <td>
                                        <div class="d-flex justify-content-center">
                                            <a href="#" class="link-dark text-decoration-none">
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${request.creator.image == null}">
                                                            <div class="rounded-circle me-2"
                                                                 style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_DEFAULT_IMG%>);background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="rounded-circle me-2"
                                                                 style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_IMG_UPLOAD_DIRECTORY%>${request.creator.image});background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:out value="${request.creator.lastName} ${request.creator.firstName}"/>
                                                </div>
                                            </a>
                                        </div>
                                    </td>
                                    <td><c:out value="${request.activity.name}" /></td>
                                    <td><fmt:message key="confirmed" bundle="${myBundle}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${request.forDelete}">
                                                <b><fmt:message key="requests.remove" bundle="${myBundle}" /></b>
                                            </c:when>
                                            <c:otherwise>
                                                <b><fmt:message key="requests.add" bundle="${myBundle}" /></b>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate type="both" dateStyle="medium" value="${request.createTime}" /></td>
                                    <td>
                                        <div>
                                            <a href="<%= request.getContextPath() + "/a/request" %>?id=${request.id}"
                                               class="btn btn-primary"><fmt:message key="show" bundle="${myBundle}" /></a>
                                        </div>
                                    </td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td>
                                        <div class="d-flex justify-content-center">
                                            <a href="#" class="link-dark text-decoration-none">
                                                <div class="d-flex align-items-center">
                                                    <c:choose>
                                                        <c:when test="${request.creator.image == null}">
                                                            <div class="rounded-circle me-2"
                                                                 style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_DEFAULT_IMG%>);background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="rounded-circle me-2"
                                                                 style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_USER_IMG_UPLOAD_DIRECTORY%>${request.creator.image});background-size: cover;background-position: center; height: 32px;width: 32px;"></div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:out value="${request.creator.lastName} ${request.creator.firstName}"/>
                                                </div>
                                            </a>
                                        </div>
                                    </td>
                                    <td><c:out value="${request.activity.name}" /></td>
                                    <td><fmt:message key="waiting" bundle="${myBundle}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${request.forDelete}">
                                                <b><fmt:message key="requests.remove" bundle="${myBundle}" /></b>
                                            </c:when>
                                            <c:otherwise>
                                                <b><fmt:message key="requests.add" bundle="${myBundle}" /></b>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate type="both" dateStyle="medium" value="${request.createTime}" /></td>
                                    <td>
                                        <div>
                                            <a href="<%= request.getContextPath() + "/a/request" %>?id=${request.id}"
                                               class="btn btn-primary"><fmt:message key="show" bundle="${myBundle}" /></a>
                                            <c:choose>
                                                <c:when test="${request.forDelete}">
                                                    <a href="<%= request.getContextPath() + "/a/conf-req-rem" %>?request_id=${request.id}"
                                                       class="btn btn-success"><fmt:message key="confirm" bundle="${myBundle}" /></a>
                                                    <a href="<%= request.getContextPath() + "/a/decl-req-rem" %>?id=${request.id}"
                                                       class="btn btn-danger"><fmt:message key="decline" bundle="${myBundle}" /></a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="<%= request.getContextPath() + "/a/conf-req-add" %>?request_id=${request.id}&activity_id=${request.activity.id}&creator_id=${request.creator.id}"
                                                       class="btn btn-success"><fmt:message key="confirm" bundle="${myBundle}" /></a>
                                                    <a href="<%= request.getContextPath() + "/a/decl-req-add" %>?id=${request.id}"
                                                       class="btn btn-danger"><fmt:message key="decline" bundle="${myBundle}" /></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="row mt-3">
        <div class="d-flex justify-content-center">
            <nav aria-label="...">
                <ul class="pagination">
                    <tg:Pagination pages="${requestScope.pageCount}" />
                </ul>
            </nav>
        </div>
    </div>
</div>

<%
    session.removeAttribute("successMessage");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
