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
                        <li><a href="<%= request.getContextPath() + "/a/activities" %>" class="dropdown-item">Show</a></li>
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
                <li><a href="<%= request.getContextPath() + "/a/requests" %>" class="nav-link px-2 link-dark">Requests</a>
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
<%-- //////////////////////////////////////////////////////////////////////// --%>
<section class="container">
    <div class="row">
        <div class="col-12 text-center"><h3>Edit category</h3></div>
        <div class="col-2"></div>
        <form action="<%= request.getContextPath() + "/a/edit-cat" %>" class="col-8 border rounded mt-2"
              method="post">
            <div class="form-group mx-4">
                <label for="categoryEN" class="col-form-label-lg">Category (EN) <p class="d-inline-block mb-0"
                                                                                   style="color: red">*</p></label>
                <input type="text" class="form-control" id="categoryEN" name="categoryEN"
                       value="${sessionScope.category.nameEN}"
                       placeholder="Category" pattern="[a-zA-Z'\d\s]+" required>
                <c:if test="${sessionScope.categoryEnError != null}">
                    <div class="alert alert-danger mt-3 py-1">${sessionScope.categoryEnError}</div>
                </c:if>
            </div>
            <div class="form-group mx-4">
                <label for="categoryUA" class="col-form-label-lg">Category (UA) <p class="d-inline-block mb-0"
                                                                                   style="color: red">*</p></label>
                <input type="text" class="form-control" id="categoryUA" name="categoryUA"
                       value="${sessionScope.category.nameUA}"
                       placeholder="Категорія" pattern="[a-zA-Zа-щА-ЩЬьЮюЯяЇїІіЄєҐґ'\d\s]+" required>
                <c:if test="${sessionScope.categoryUaError != null}">
                    <div class="alert alert-danger mt-3 py-1">${sessionScope.categoryUaError}</div>
                </c:if>
            </div>
            <div class="row form-group mx-4 my-3">
                <button class="btn btn-lg btn-primary" type="submit" name="sendMe" value="1">Edit</button>
            </div>
        </form>
        <div class="col-2"></div>
    </div>
</section>

<%
    session.removeAttribute("categoryEN");
    session.removeAttribute("categoryUA");
    session.removeAttribute("categoryEnError");
    session.removeAttribute("categoryUaError");
%>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</body>
</html>
