<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ page import="com.tracking.lang.Language" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mtl" uri="/myTLD" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="h-100">
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <c:set var="lang" value="${sessionScope.lang.split('_')[0]}" />
    <title>${requestScope.activity.name}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100%;
            width: 100%;
        }

        <c:choose>
        <c:when test="${requestScope.activity.image == null}">
        .activity-image {
            width: 100%;
            min-height: 100vh;
            background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_ACTIVITY_DEFAULT_IMG%>);
            background-repeat: no-repeat;
            background-size: 100% auto;
            background-position: center top;
            background-attachment: fixed;
        }

        </c:when>
        <c:otherwise>
        .activity-image {
            width: 100%;
            min-height: 100vh;
            background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_ACTIVITY_IMG_UPLOAD_DIRECTORY%>${requestScope.activity.image});
            background-repeat: no-repeat;
            background-size: 100% auto;
            background-position: center top;
            background-attachment: fixed;
        }

        </c:otherwise>
        </c:choose>
    </style>
</head>
<body>

<header class="p-3 border-bottom">
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
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/u/activity" %>?id=${param.id}&lang=en_EN">EN</a></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/u/activity" %>?id=${param.id}&lang=uk_UA">UA</a></li>
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

<div class="activity-image">
    <div class="container" style="min-height: 100vh; background-color: #eee;">
        <div class="row text-center pt-2">
            <h1><c:out value="${requestScope.activity.name}" /></h1>
        </div>
        <hr class="mt-1">
        <div class="row px-4">
            <h4><fmt:message key="description" bundle="${myBundle}" />:</h4>
            <p><c:out value="${requestScope.activity.description}" /></p>
            <div class="d-flex align-items-center mb-2">
                <h4><fmt:message key="category" bundle="${myBundle}" />: </h4>
                <p class="mb-2 ms-2" style="font-size: 1.2rem">
                    <mtl:setCategories categoryList="${requestScope.categories}" lang="${lang}" />
                </p>
            </div>
        </div>
        <hr class="mt-1">
        <c:if test="${requestScope.userActivity != null}">
            <div class="row px-4">
                <h4><fmt:message key="my_act_info" bundle="${myBundle}" />: </h4>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b><fmt:message key="status" bundle="${myBundle}" />:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.status.value.equals('NOT_STARTED')}">
                            <fmt:message key="not_started" bundle="${myBundle}" />
                        </c:when>
                        <c:when test="${requestScope.userActivity.status.value.equals('STARTED')}">
                            <fmt:message key="started" bundle="${myBundle}" />
                        </c:when>
                        <c:when test="${requestScope.userActivity.status.value.equals('STOPPED')}">
                            <fmt:message key="stopped" bundle="${myBundle}" />
                        </c:when>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b><fmt:message key="start_time" bundle="${myBundle}" />:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.startTime != null}">
                            <fmt:formatDate type="both" dateStyle="medium" value="${requestScope.userActivity.startTime}" />
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b><fmt:message key="stop_time" bundle="${myBundle}" />:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.stopTime != null &&
                                                requestScope.userActivity.status.value.equals('STOPPED')}">
                            <fmt:formatDate type="both" dateStyle="medium" value="${requestScope.userActivity.stopTime}" />
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b><fmt:message key="total_spent_time" bundle="${myBundle}" />:</b>
                        <fmt:formatNumber type="number" maxFractionDigits="1" value="${requestScope.userActivity.spentTime}" /> <fmt:message key="hours" bundle="${myBundle}" />
                </p>
                <div class="mb-3">
                    <c:choose>
                        <c:when test="${requestScope.userActivity.status.value.equals('STARTED')}">
                            <a href="<%=request.getContextPath() + "/u/activity-start"%>?id=${requestScope.activity.id}"
                               class="btn btn-primary disabled"><fmt:message key="start" bundle="${myBundle}" /></a>
                            <a href="<%=request.getContextPath() + "/u/activity-stop"%>?id=${requestScope.activity.id}"
                               class="btn btn-danger"><fmt:message key="stop" bundle="${myBundle}" /></a>
                        </c:when>
                        <c:otherwise>
                            <a href="<%=request.getContextPath() + "/u/activity-start"%>?id=${requestScope.activity.id}"
                               class="btn btn-primary"><fmt:message key="start" bundle="${myBundle}" /></a>
                            <a href="<%=request.getContextPath() + "/u/activity-stop"%>?id=${requestScope.activity.id}"
                               class="btn btn-danger disabled"><fmt:message key="stop" bundle="${myBundle}" /></a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <hr class="mt-1">
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
