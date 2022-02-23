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
        <h1>Categories</h1>
    </div>
    <c:if test="${sessionScope.successMessage != null}">
        <div class="row mt-2">
            <div class="alert alert-success py-1">${sessionScope.successMessage}</div>
        </div>
    </c:if>
    <div class="row">
        <div class="d-inline-flex justify-content-between">
            <div class="d-flex">
                <form action="<%= request.getContextPath() + "/a/categories" %>"
                      class="d-flex col-12 col-lg-auto mx-0 mb-2 justify-content-center align-items-center"
                      method="get">
                    <select class="form-select w-25" aria-label="Lang" name="lang" id="lang">
                        <c:choose>
                            <c:when test="${param.lang.equals('en')}">
                                <option value="en" selected>EN</option>
                            </c:when>
                            <c:otherwise>
                                <option value="en">EN</option>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${param.lang.equals('ua')}">
                                <option value="ua" selected>UA</option>
                            </c:when>
                            <c:otherwise>
                                <option value="ua">UA</option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                    <input type="search" name="name" class="form-control ms-2" placeholder="Category"
                           aria-label="Category" value="${param.name}">
                    <button type="submit" class="btn btn-primary ms-2">Search</button>
                </form>
            </div>
            <div class="d-flex">
                <form action="<%=request.getContextPath() + "/a/categories"%>"
                      class="d-flex col-12 col-lg-auto mx-0 mb-2 justify-content-center align-items-center"
                      method="get">
                    <c:if test="${param.lang != null && not empty param.lang}">
                        <input type="hidden" name="lang" value="${param.lang}">
                    </c:if>
                    <c:if test="${param.name != null && not empty param.name}">
                        <input type="hidden" name="name" value="${param.name}">
                    </c:if>
                    <div class="d-flex">
                        <a href="<%= request.getContextPath() + "/a/categories" %>" class="btn btn-primary">Reset</a>
                        <select class="form-select ms-2" aria-label="Sort by" name="sort" id="sort">
                            <option value="">Sort by</option>
                            <c:choose>
                                <c:when test="${param.sort.equals('id')}">
                                    <option value="id" selected>id</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="id">id</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('name_en')}">
                                    <option value="name_en" selected>name (EN)</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="name_en">name (EN)</option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('name_ua')}">
                                    <option value="name_ua" selected>name (UA)</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="name_ua">name (UA)</option>
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
        <div>
            <table class="table text-center align-middle table-bordered table-striped mt-2">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Name (EN)</th>
                    <th>Name (UA)</th>
                    <th class="w-25">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="category" items="${requestScope.categoryList}">
                    <tr>
                        <td><c:out value="${category.id}"/></td>
                        <td><c:out value="${category.nameEN}"/></td>
                        <td><c:out value="${category.nameUA}"/></td>
                        <td>
                            <div>
                                <c:if test="${category.id > 1}">
                                    <a href="<%= request.getContextPath() + "/a/edit-cat" %>?id=${category.id}"
                                       class="btn btn-primary">Edit</a>
                                    <a href="<%= request.getContextPath() + "/a/delete-cat" %>?id=${category.id}"
                                       class="btn btn-danger">Delete</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
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
