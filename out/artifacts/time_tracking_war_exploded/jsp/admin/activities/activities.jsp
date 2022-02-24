<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ page import="java.util.Enumeration" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
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
                    <a href="#" class="nav-link px-2 link-secondary" id="dropdownActivities" data-bs-toggle="dropdown" aria-expanded="false">Activities</a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownActivities">
                        <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item">Show</a></li>
                        <li><a href="<%= request.getContextPath() + "/a/add-act" %>" class="dropdown-item">Add</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#" class="nav-link px-2 link-dark" id="dropdownCategories" data-bs-toggle="dropdown" aria-expanded="false">Categories</a>
                    <ul class="dropdown-menu text-small" aria-labelledby="dropdownCategories">
                        <li><a href="<%= request.getContextPath() + "/a/categories" %>" class="dropdown-item">Show</a></li>
                        <li><a href="<%= request.getContextPath() + "/a/add-cat" %>" class="dropdown-item">Add</a></li>
                    </ul>
                </li>
                <li><a href="<%= request.getContextPath() + "/a/users" %>" class="nav-link px-2 link-dark">Users</a></li>
                <li><a href="<%= request.getContextPath() + "/a/requests" %>" class="nav-link px-2 link-dark">Requests</a></li>
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
                    <li><a class="dropdown-item" href="#">Profile</a></li>
                    <li><a class="dropdown-item" href="#">New activity</a></li>
                    <li><a class="dropdown-item" href="#">New user</a></li>
                    <li><a class="dropdown-item" href="#">Settings</a></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="<%= request.getContextPath() + "/logout" %>">Sign out</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>

<div class="container">
    <div class="row mb-2">
        <div class="col-12 text-center"><h1>Activities</h1></div>
        <c:if test="${sessionScope.successMessage != null}">
            <div class="col-12 mt-2">
                <div class="alert alert-success py-1">${sessionScope.successMessage}</div>
            </div>
        </c:if>
    </div>
    <div class="row">
        <h4>Search:</h4>
    </div>
    <div class="row">
        <form action="<%= request.getContextPath() + "/a/activities" %>"
              class="d-flex col-12 col-lg-auto me-lg-auto mb-2 justify-content-center align-items-center w-50" method="get">
            <input type="search" name="search" class="form-control" placeholder="Search..." aria-label="Search" value="${requestScope.searchQuery}">
            <button type="submit" class="btn btn-primary ms-2">Search</button>
        </form>
    </div>
    <form action="<%= request.getContextPath() + "/a/activities" %>" method="get">
        <div class="row">
            <h4>Categories:</h4>
        </div>
        <c:if test="${param.search != null}">
            <input type="hidden" name="search" value="${param.search}">
        </c:if>
        <div class="row row-cols-6 g-0 mb-2">
            <c:forEach var="category" items="${requestScope.categories}">
                <div class="form-check">
                    <c:choose>
                        <c:when test="${requestScope.filterCategories.contains(category.id)}">
                            <input type="checkbox" class="form-check-input" name="filter"
                                   value="${category.id}" id="cat${category.id}" checked>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" class="form-check-input" name="filter" value="${category.id}"
                                   id="cat${category.id}">
                        </c:otherwise>
                    </c:choose>
                    <label for="cat${category.id}" class="form-check-label"> <%-- localize --%>
                            ${category.nameEN}
                    </label>
                </div>
            </c:forEach>
        </div>
        <div class="row">
            <h4>People:</h4>
        </div>
        <div class="row">
            <div class="d-inline-flex justify-content-between">
                <div class="d-flex">
                    <input type="number" class="form-control me-1" style="width: 100px;" name="from" placeholder="From" aria-label="From" min="0" max="${requestScope.peopleMaxCount}" value="${param.from}">
                    <input type="number" class="form-control ms-1" style="width: 100px;" name="to" placeholder="To" aria-label="To" min="0" max="${requestScope.peopleMaxCount}" value="${param.to}">
                    <button type="submit" class="btn btn-primary ms-2">Filter</button>
                    <a href="<%= request.getContextPath() + "/a/activities" %>" class="btn btn-primary ms-1">Reset</a>
                </div>
            </div>
        </div>
    </form>
    <div class="row">
        <div class="d-inline-flex justify-content-between">
            <div class="d-flex"></div>
            <div class="d-flex align-items-center">
                <form action="<%= request.getContextPath() + "/a/activities" %>" method="get">
                    <c:if test="${param.search != null && not empty param.search}">
                        <input type="hidden" name="search" value="${param.search}">
                    </c:if>
                    <c:if test="${param.filter != null && not empty param.filter}">
                        <c:forEach var="category" items="${requestScope.filterCategories}">
                            <input type="hidden" name="filter" value="${category}">
                        </c:forEach>
                    </c:if>
                    <c:if test="${param.from != null && not empty param.from}">
                        <input type="hidden" name="from" value="${param.from}">
                    </c:if>
                    <c:if test="${param.to != null && not empty param.to}">
                        <input type="hidden" name="to" value="${param.to}">
                    </c:if>
                    <div class="d-flex">
                        <select class="form-select" aria-label="Sort by" name="sort" id="sort">
                            <option value="">Sort by</option>
                            <c:choose>
                                <c:when test="${param.sort.equals('name')}">
                                    <option value="name" selected>alphabet</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="name">alphabet</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('create_time')}">
                                    <option value="create_time" selected>newest</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="create_time">newest</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('people_count')}">
                                    <option value="people_count" selected>number of people</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="people_count">number of people</option>
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
                                value="2">
                            Sort
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
        <c:forEach var="activity" items="${requestScope.activities}">
            <div class="col">
                <div class="card shadow-sm">
<%--                    ${pageContext.request.contextPath}<%=FilePaths.GET_ACTIVITY_DEFAULT_IMG%>--%>
                    <c:choose>
                        <c:when test="${activity.image == null}">
                            <a href="<%=request.getContextPath() + "/a/activity"%>?id=${activity.id}"
                               class="text-black text-decoration-none">
                                <div class="bd-placeholder-img card-img-top w-100"
                                     style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_ACTIVITY_DEFAULT_IMG%>);height: 225px;"></div>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="<%=request.getContextPath() + "/a/activity"%>?id=${activity.id}"
                               class="text-black text-decoration-none">
                                <div class="bd-placeholder-img card-img-top w-100"
                                     style="background-image: url(${pageContext.request.contextPath}<%=FilePaths.GET_ACTIVITY_IMG_UPLOAD_DIRECTORY%>${activity.image});height: 225px;background-size: cover;"></div>
                            </a>
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body">
                        <h5><a href="<%--{{ route('admin.showActivity', ['id' => $item->id]) }}--%>"
                               class="text-black text-decoration-none">${activity.name}</a></h5>
                        <h6 class="opacity-75">
                            <c:choose>
                                <c:when test="${activity.categories == null || activity.categories.isEmpty()}"> <%-- localize --%>
                                    <c:forEach var="category" items="${requestScope.categories}">
                                        <c:if test="${category.id == 0}">
                                            ${category.nameEN}
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="i" value="0" scope="page" />
                                    <c:forEach var="category" items="${requestScope.categories}" varStatus="loop">
                                        <c:if test="${activity.categories.contains(category.id)}">
                                            <c:set var="i" value="${i + 1}" scope="page" />
                                            ${category.nameEN}<c:if test="${i != activity.categories.size()}">, </c:if>
                                        </c:if>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </h6>
                        <p class="card-text">${activity.description}</p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div class="btn-group">
                                <a href="<%=request.getContextPath() + "/a/activity"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary">Open</a>
                                <a href="<%=request.getContextPath() + "/a/edit-act"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary">Edit</a>
                                <a href="<%=request.getContextPath() + "/a/delete-act"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary">Delete</a>
                            </div>

                            <div class="d-flex text-muted">
                                <p class="m-0">${activity.createTime}</p>
                            </div>

                            <div class="d-flex text-muted">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
                                </svg>
                                <small><b>${activity.peopleCount}</b></small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<div class="container mt-3">
    <div class="row">
        <div class="d-flex justify-content-center">
            <nav aria-label="...">
                <ul class="pagination">
                    <c:if test="${requestScope.pageCount > 1}">
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
                                        <a class="page-link" href="${requestScope.queryForPagination}&page=${requestScope.previousPage}">Previous</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="page-link" href="?page=${requestScope.previousPage}">Previous</a>
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
                                                <a class="page-link" href="${requestScope.queryForPagination}&page=${i}">${i}</a>
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
                                        <a class="page-link" href="${requestScope.queryForPagination}&page=${requestScope.nextPage}">Next</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="page-link" href="?page=${requestScope.nextPage}">Next</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>

<%
    session.removeAttribute("successMessage");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
