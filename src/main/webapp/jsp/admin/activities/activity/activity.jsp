<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html class="h-100">
<head>
    <title>Title</title>
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
            <a href="<%= request.getContextPath() + "/a/activities" %>"
               class="d-flex align-items-center mb-2 mb-lg-0 text-dark text-decoration-none">
                <svg xmlns="http://www.w3.org/2000/svg" width="40" height="32" class="me-2" viewBox="0 0 118 94"
                     role="img"><title>Bootstrap</title>
                    <path fill-rule="evenodd" clip-rule="evenodd"
                          d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"
                          fill="currentColor"></path>
                </svg>
            </a>

            <%--            {{--            <form class="col-12 col-lg-auto ms-3 me-lg-auto mb-2 justify-content-center mb-md-0 w-50">--}}--%>
            <%--            {{--                <input type="search" class="form-control" placeholder="Search..." aria-label="Search">--}}--%>
            <%--            {{--            </form>--}}--%>
            <div class="me-lg-auto"></div>
            <ul class="nav col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                <li>
                    <a href="#" class="nav-link px-2 link-secondary" id="dropdownActivities" data-bs-toggle="dropdown"
                       aria-expanded="false">Activities</a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownActivities">
                        <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item">Show</a>
                        </li>
                        <li><a href="<%= request.getContextPath() + "/a/add-act" %>" class="dropdown-item">Add</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="#" class="nav-link px-2 link-dark" id="dropdownCategories" data-bs-toggle="dropdown"
                       aria-expanded="false">Categories</a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownCategories">
                        <li><a href="<%= request.getContextPath() + "/a/categories" %>"
                               class="dropdown-item">Show</a></li>
                        <li><a href="<%= request.getContextPath() + "/a/add-cat" %>" class="dropdown-item">Add</a>
                        </li>
                    </ul>
                </li>
                <li><a href="<%= request.getContextPath() + "/a/users" %>" class="nav-link px-2 link-dark">Users</a>
                </li>
                <li><a href="<%= request.getContextPath() + "/a/requests" %>"
                       class="nav-link px-2 link-dark">Requests</a>
                </li>
            </ul>

            <div class="dropdown text-end ps-4 border-start">
                <a href="#" class="d-block link-dark text-decoration-none dropdown-toggle" id="dropdownUser1"
                   data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="https://github.com/mdo.png" alt="mdo" width="32" height="32" class="rounded-circle me-1">
                    <c:out value="${sessionScope.authUser.lastName} ${sessionScope.authUser.firstName}"/>
                </a>
                <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser1">
                    <li><a class="dropdown-item" href="#">Profile</a></li>
                    <li><a class="dropdown-item" href="#">New activity</a></li>
                    <li><a class="dropdown-item" href="#">New user</a></li>
                    <li><a class="dropdown-item" href="#">Settings</a></li>
                    <li>
                        <hr class="dropdown-divider">
                    </li>
                    <li><a class="dropdown-item" href="<%= request.getContextPath() + "/logout" %>">Sign out</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>

<div class="activity-image">
    <div class="container" style="min-height: 100vh; background-color: #eee;">
        <div class="row text-center pt-2">
            <h1>${requestScope.activity.name}</h1>
        </div>
        <hr class="mt-1">
        <div class="row px-4">
            <h4>Description:</h4>
            <p>${requestScope.activity.description}</p>
        </div>
        <hr class="mt-1">
        <c:if test="${requestScope.userActivity != null}">
            <div class="row px-4">
                <h4>My activity information: </h4>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b>Status:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.status.value.equals('NOT_STARTED')}">
                            not started
                        </c:when>
                        <c:when test="${requestScope.userActivity.status.value.equals('STARTED')}">
                            started
                        </c:when>
                        <c:when test="${requestScope.userActivity.status.value.equals('STOPPED')}">
                            stopped
                        </c:when>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b>Start time:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.startTime != null}">
                            ${requestScope.userActivity.startTime}
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b>Stop time:</b>
                    <c:choose>
                        <c:when test="${requestScope.userActivity.stopTime != null &&
                                        requestScope.userActivity.status.value.equals('STOPPED')}">
                            ${requestScope.userActivity.stopTime}
                        </c:when>
                        <c:otherwise>
                            -
                        </c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-1" style="font-size: 1.2rem">
                    <b>Total spent time:</b>
                        ${requestScope.userActivity.spentTime} hours
                </p>
                <div class="mb-3">
                    <c:choose>
                        <c:when test="${requestScope.userActivity.status.value.equals('STARTED')}">
                            <a href="<%=request.getContextPath() + "/a/activity-start"%>?id=${requestScope.activity.id}"
                               class="btn btn-primary disabled">Start</a>
                            <a href="<%=request.getContextPath() + "/a/activity-stop"%>?id=${requestScope.activity.id}"
                               class="btn btn-danger">Stop</a>
                        </c:when>
                        <c:otherwise>
                            <a href="<%=request.getContextPath() + "/a/activity-start"%>?id=${requestScope.activity.id}"
                               class="btn btn-primary">Start</a>
                            <a href="<%=request.getContextPath() + "/a/activity-stop"%>?id=${requestScope.activity.id}"
                               class="btn btn-danger disabled">Stop</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <hr class="mt-1">
        </c:if>
        <div class="row px-4">
            <h4>Creator: </h4>
            <a href="#">
                <p style="font-size: 1.2rem">${requestScope.creator.lastName} ${requestScope.creator.firstName}</p>
            </a>
        </div>
        <hr class="mt-1">
        <div class="row px-4">
            <h4>Available users: </h4>
            <form action="<%=request.getContextPath() + "/a/activity-add-user"%>?activity=${requestScope.activity.id}"
                  method="POST">
                <div class="form-group d-flex">
                    <select class="form-select w-25" aria-label="Select User" name="user" id="user"
                            required>
                        <option value="">Select User</option>
                        <c:forEach var="user" items="${requestScope.usersNotInActivity}">
                            <c:choose>
                                <c:when test="${user.admin}">
                                    <option value="${user.id}">${user.lastName} ${user.firstName} (admin)</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${user.id}">${user.lastName} ${user.firstName}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <button type="submit" class="btn btn-primary" style="width: 100px; margin-left: 11px;" value="1">
                        Add
                    </button>
                </div>
            </form>
        </div>
        <c:choose>
            <c:when test="${requestScope.users != null && not empty requestScope.users}">
                <hr class="mt-1">
                <div class="row px-4">
                    <h4>Participants: </h4>
                    <table class="table text-center align-middle table-bordered table-striped mt-2">
                        <thead>
                        <tr>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Start time</th>
                            <th>Stop time</th>
                            <th>Spent time</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="user" items="${requestScope.users}">
                            <c:choose>
                                <c:when test="${user.admin}">
                                    <tr>
                                        <td class="table-primary">${user.userId}</td>
                                        <td class="table-primary">
                                            <div class="d-flex justify-content-center">
                                                <a href="#" class="link-dark text-decoration-none">
                                                    <div class="d-flex align-items-center">
                                                            <%--                                    @if($item->image === null)--%>
                                                            <%--                                    <div class="rounded-circle me-2"--%>
                                                            <%--                                         style="background-image: url({{ url('img/default/user/user.png') }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                            <%--                                    @else--%>
                                                            <%--                                    <div class="rounded-circle me-2"--%>
                                                            <%--                                         style="background-image: url({{ url('img/upload/users/'.$item->image) }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                            <%--                                    @endif--%>
                                                            ${user.userLastName} ${user.userFirstName}
                                                    </div>
                                                </a>
                                            </div>
                                        </td>
                                        <td class="table-primary">
                                            <c:choose>
                                                <c:when test="${user.startTime == null}">
                                                    -
                                                </c:when>
                                                <c:otherwise>
                                                    ${user.startTime}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="table-primary">
                                            <c:choose>
                                                <c:when test="${user.stopTime != null && requestScope.userActivity.status.value.equals('STOPPED')}">
                                                    ${user.stopTime}
                                                </c:when>
                                                <c:otherwise>
                                                    -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="table-primary">
                                                ${user.spentTime} hours
                                        </td>
                                        <td class="table-primary">
                                            <c:choose>
                                                <c:when test="${user.status.value.equals('NOT_STARTED')}">
                                                    not started
                                                </c:when>
                                                <c:when test="${user.status.value.equals('STARTED')}">
                                                    started
                                                </c:when>
                                                <c:when test="${user.status.value.equals('STOPPED')}">
                                                    stopped
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td class="table-primary">
                                            <div>
                                                <a href="<%=request.getContextPath() +
                                        "/a/activity-remove-user"%>?activity=${requestScope.activity.id}&user=${user.userId}"
                                                   class="btn btn-danger">Remove</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td>${user.userId}</td>
                                        <td>
                                            <div class="d-flex justify-content-center">
                                                <a href="#" class="link-dark text-decoration-none">
                                                    <div class="d-flex align-items-center">
                                                            <%--                                    @if($item->image === null)--%>
                                                            <%--                                    <div class="rounded-circle me-2"--%>
                                                            <%--                                         style="background-image: url({{ url('img/default/user/user.png') }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                            <%--                                    @else--%>
                                                            <%--                                    <div class="rounded-circle me-2"--%>
                                                            <%--                                         style="background-image: url({{ url('img/upload/users/'.$item->image) }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                            <%--                                    @endif--%>
                                                            ${user.userLastName} ${user.userFirstName}
                                                    </div>
                                                </a>
                                            </div>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.startTime == null}">
                                                    -
                                                </c:when>
                                                <c:otherwise>
                                                    ${user.startTime}
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.stopTime != null && requestScope.userActivity.status.value.equals('STOPPED')}">
                                                    ${user.stopTime}
                                                </c:when>
                                                <c:otherwise>
                                                    -
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                                ${user.spentTime} hours
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${user.status.value.equals('NOT_STARTED')}">
                                                    not started
                                                </c:when>
                                                <c:when test="${user.status.value.equals('STARTED')}">
                                                    started
                                                </c:when>
                                                <c:when test="${user.status.value.equals('STOPPED')}">
                                                    stopped
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div>
                                                <a href="<%=request.getContextPath() +
                                        "/a/activity-remove-user"%>?activity=${requestScope.activity.id}&user=${user.userId}"
                                                   class="btn btn-danger">Remove</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <hr class="mt-1">
                <div class="row px-4 text-center mt-5">
                    <h4 class="text-muted">This activity don't have participants. You can add users above.</h4>
                </div>
            </c:otherwise>
        </c:choose>

        <c:if test="${requestScope.pageCount > 1}">
            <div class="row mt-3">
                <div class="d-flex justify-content-center">
                    <nav aria-label="...">
                        <ul class="pagination">
                                <%--                        Previous Button --%>
                            <c:choose>
                                <c:when test="${param.page == null || param.page == 1}">
                                    <li class="page-item disabled">
                                        <span class="page-link">Previous</span>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <a class="page-link"
                                       href="<%=request.getContextPath() + "/a/activity"%>?id=${requestScope.activity.id}&page=${requestScope.previousPage}">
                                        Previous
                                    </a>
                                </c:otherwise>
                            </c:choose>
                                <%--                        Page Buttons --%>
                            <c:forEach var="i" begin="1" end="${requestScope.pageCount}">
                                <c:choose>
                                    <c:when test="${(param.page == null && i == 1) || (param.page != null && i == param.page)}">
                                        <li class="page-item active" aria-current="page">
                                            <span class="page-link">${i}</span>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="<%=request.getContextPath() + "/a/activity"%>?id=${requestScope.activity.id}&page=${i}">
                                                    ${i}
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                                <%--                        Next Button--%>
                            <c:choose>
                                <c:when test="${param.page == requestScope.pageCount}">
                                    <li class="page-item disabled">
                                        <a class="page-link" href="#">Next</a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <a class="page-link"
                                       href="<%=request.getContextPath() + "/a/activity"%>?id=${requestScope.activity.id}&page=${requestScope.nextPage}">Next</a>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </nav>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
