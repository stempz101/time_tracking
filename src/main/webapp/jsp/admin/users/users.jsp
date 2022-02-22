<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <title>Title</title>
</head>
<body>

<header class="p-3 mb-3 border-bottom">
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
                    <a href="#" class="nav-link px-2 link-dark" id="dropdownActivities" data-bs-toggle="dropdown"
                       aria-expanded="false">Activities</a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownActivities">
                        <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item">Show</a>
                        </li>
                        <li><a href="<%= request.getContextPath() + "/a/add-act" %>" class="dropdown-item">Add</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="#" class="nav-link px-2 link-secondary" id="dropdownCategories" data-bs-toggle="dropdown"
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

<section class="container">
    <div class="row text-center">
        <h1>Users</h1>
    </div>
    <div class="row">
        <h4>Search:</h4>
    </div>
    <div class="row">
        <div class="d-inline-flex justify-content-between">
            <div class="d-flex">
                <form action="<%= request.getContextPath() + "/a/users" %>"
                      class="d-flex col-12 col-lg-auto mx-0 mb-2 justify-content-center align-items-center"
                      method="get">
                    <input type="search" name="lastName" class="form-control w-50" placeholder="Smith"
                           aria-label="Last Name" value="${param.lastName}">
                    <input type="search" name="firstName" class="form-control w-50 ms-2" placeholder="John"
                           aria-label="First Name" value="${param.firstName}">
                    <button type="submit" class="btn btn-primary ms-2">Search</button>
                </form>
            </div>
            <div class="d-flex align-items-center">
                <form action="<%=request.getContextPath() + "/a/users"%>" method="get">
                    <c:if test="${param.lastName != null && not empty param.lastName}">
                        <input type="hidden" name="lastName" value="${param.lastName}">
                    </c:if>
                    <c:if test="${param.firstName != null && not empty param.firstName}">
                        <input type="hidden" name="firstName" value="${param.firstName}">
                    </c:if>
                    <div class="d-flex">
                        <select class="form-select" aria-label="Sort by" name="sortBy" id="sort">
                            <option value="">Sort by</option>
                            <c:choose>
                                <c:when test="${param.sortBy.equals('id')}">
                                    <option value="id" selected>id</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="id">id</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sortBy.equals('last_name')}">
                                    <option value="last_name" selected>name</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="last_name">name</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sortBy.equals('activity_count')}">
                                    <option value="activity_count" selected>activities</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="activity_count">activities</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sortBy.equals('spent_time')}">
                                    <option value="spent_time" selected>time</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="spent_time">time</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sortBy.equals('is_admin')}">
                                    <option value="is_admin" selected>role</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="is_admin">role</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                        <select class="form-select ms-2" aria-label="Order by" name="order" id="orderBy"
                                required>
                            <c:choose>
                                <c:when test="${param.order.equals('asc') ||
                                                param.order == null ||
                                                empty param.order}">
                                    <option value="asc" selected>ascending</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="asc">ascending</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.order.equals('desc')}">
                                    <option value="desc" selected>descending</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="desc">descending</option>
                                </c:otherwise>
                            </c:choose>
                        </select>
                        <button type="submit" class="btn btn-primary" style="width: 100px; margin-left: 11px;"
                                value="1">
                            Sort
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="">
            <table class="table text-center align-middle table-bordered table-striped mt-2">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Activities</th>
                    <th>Spent time</th>
                    <th>Role</th>
                    <th>Set/Unset Admin</th>
                    <th>Block/Unblock</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${requestScope.userList}">
                    <c:choose>
                        <c:when test="${user.admin}">
                            <tr>
                                <td class="table-primary">${user.id}</td>
                                <td class="table-primary">
                                    <div class="d-flex justify-content-center">
                                        <a href="#" class="link-dark text-decoration-none">
                                            <div class="d-flex align-items-center">
                                                    <%--                                                @if($item->image === null)--%>
                                                    <%--                                                <div class="rounded-circle me-2"--%>
                                                    <%--                                                     style="background-image: url({{ url('img/default/user/user.png') }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                    <%--                                                @else--%>
                                                    <%--                                                <div class="rounded-circle me-2"--%>
                                                    <%--                                                     style="background-image: url({{ url('img/upload/users/'.$item->image) }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                    <%--                                                @endif--%>
                                                    ${user.lastName} ${user.firstName}
                                            </div>
                                        </a>
                                    </div>
                                </td>
                                <td class="table-primary">${user.activityCount}</td>
                                <td class="table-primary">${user.spentTime} hours</td>
                                <td class="table-primary">admin</td>
                                <td class="table-primary">
                                    <div>
                                        <c:choose>
                                            <c:when test="${user.id == sessionScope.authUser.id}">
                                                <a href="<%=request.getContextPath()
                                                            + "/a/user-set-admin"%>?id=${user.id}&value=false"
                                                   class="btn btn-danger disabled">Unset</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<%=request.getContextPath()
                                                            + "/a/user-set-admin"%>?id=${user.id}&value=false"
                                                   class="btn btn-danger">Unset</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                                <td class="table-primary">
                                    <div>
                                        <c:choose>
                                            <c:when test="${user.id == sessionScope.authUser.id}">
                                                <a href="<%=request.getContextPath()
                                                            + "/a/user-set-block"%>?id=${user.id}&value=true"
                                                   class="btn btn-danger disabled">Block</a>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${user.blocked}">
                                                        <a href="<%=request.getContextPath()
                                                            + "/a/user-set-block"%>?id=${user.id}&value=false"
                                                           class="btn btn-success">Unblock</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="<%=request.getContextPath()
                                                            + "/a/user-set-block"%>?id=${user.id}&value=true"
                                                           class="btn btn-danger">Block</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td>${user.id}</td>
                                <td>
                                    <div class="d-flex justify-content-center">
                                        <a href="#" class="link-dark text-decoration-none">
                                            <div class="d-flex align-items-center">
                                                    <%--                                                @if($item->image === null)--%>
                                                    <%--                                                <div class="rounded-circle me-2"--%>
                                                    <%--                                                     style="background-image: url({{ url('img/default/user/user.png') }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                    <%--                                                @else--%>
                                                    <%--                                                <div class="rounded-circle me-2"--%>
                                                    <%--                                                     style="background-image: url({{ url('img/upload/users/'.$item->image) }});background-size: cover;background-position: center;height: 32px;width: 32px;"></div>--%>
                                                    <%--                                                @endif--%>
                                                    ${user.lastName} ${user.firstName}
                                            </div>
                                        </a>
                                    </div>
                                </td>
                                <td>${user.activityCount}</td>
                                <td>${user.spentTime} hours</td>
                                <td>user</td>
                                <td>
                                    <div>
                                        <a href="<%=request.getContextPath()
                                                            + "/a/user-set-admin"%>?id=${user.id}&value=true"
                                           class="btn btn-primary">Set</a>
                                    </div>
                                </td>
                                <td>
                                    <div>
                                        <c:choose>
                                            <c:when test="${user.blocked}">
                                                <a href="<%=request.getContextPath()
                                                            + "/a/user-set-block"%>?id=${user.id}&value=false"
                                                   class="btn btn-success">Unblock</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<%=request.getContextPath()
                                                            + "/a/user-set-block"%>?id=${user.id}&value=true"
                                                   class="btn btn-danger">Block</a>
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
                                <c:choose>
                                    <c:when test="${pageContext.request.queryString != null}">
                                        <a class="page-link"
                                           href="${requestScope.queryForPagination}&page=${requestScope.previousPage}">
                                            Previous
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="page-link" href="?page=${requestScope.previousPage}">
                                            Previous
                                        </a>
                                    </c:otherwise>
                                </c:choose>
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
                                        <c:choose>
                                            <c:when test="${pageContext.request.queryString != null}">
                                                <a class="page-link"
                                                   href="${requestScope.queryForPagination}&page=${i}">${i}</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="page-link" href="?page=${i}">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
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
                                <c:choose>
                                    <c:when test="${pageContext.request.queryString != null}">
                                        <a class="page-link"
                                           href="${requestScope.queryForPagination}&page=${requestScope.nextPage}">Next</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="page-link" href="?page=${requestScope.nextPage}">Next</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </nav>
            </div>
        </div>
    </c:if>
</section>

<%
    session.removeAttribute("successMessage");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
