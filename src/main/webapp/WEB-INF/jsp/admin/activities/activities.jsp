<%@ page import="com.tracking.controllers.constants.FilePaths" %>
<%@ page import="com.tracking.lang.Language" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="mtl" uri="/myTLD" %>
<%@ taglib prefix="tg" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <fmt:setLocale value="${sessionScope.lang}" />
    <fmt:setBundle var="myBundle" basename="content" />
    <c:set var="lang" value="${sessionScope.lang.split('_')[0]}" />
    <title><fmt:message key="activities" bundle="${myBundle}" /></title>
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
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/activities" %>?lang=en_EN">EN</a></li>
                        <li><a class="dropdown-item" href="<%= request.getContextPath() + "/a/activities" %>?lang=uk_UA">UA</a></li>
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
        <div class="col-12 text-center"><h1><fmt:message key="activities" bundle="${myBundle}" /></h1></div>
        <c:if test="${sessionScope.successMessage != null}">
            <div class="col-12 mt-2">
                <div class="alert alert-success py-1"><c:out value="${sessionScope.successMessage}" /></div>
            </div>
        </c:if>
    </div>

<%--    <c:choose>--%>
<%--        <c:when test="${requestScope.activities == null || empty requestScope.activities}">--%>
<%--            <div class="row mb-2">--%>
<%--                <div class="col-12 text-center"><h3 class="opacity-75"><fmt:message key="no_activities" bundle="${myBundle}" /></h3>--%>
<%--                    <a href="${pageContext.request.contextPath}/a/add-act" class="h3"><fmt:message key="add_activity" bundle="${myBundle}" /></a></div>--%>
<%--            </div>--%>
<%--        </c:when>--%>
<%--        <c:otherwise>--%>
<%--            --%>
<%--        </c:otherwise>--%>
<%--    </c:choose>--%>

    <div class="row">
        <h4><fmt:message key="search" bundle="${myBundle}" />:</h4>
    </div>
    <div class="row">
        <form action="<%= request.getContextPath() + "/a/activities" %>"
              class="d-flex col-12 col-lg-auto me-lg-auto mb-2 justify-content-center align-items-center w-50" method="get">
            <input type="search" name="search" class="form-control" placeholder="<fmt:message key="search" bundle="${myBundle}" />..." aria-label="Search" value="${requestScope.searchQuery}">
            <button type="submit" class="btn btn-primary ms-2"><fmt:message key="search" bundle="${myBundle}" /></button>
        </form>
    </div>
    <form action="<%= request.getContextPath() + "/a/activities" %>" method="get">
        <div class="row">
            <h4><fmt:message key="categories" bundle="${myBundle}" />:</h4>
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
        <div class="row">
            <h4><fmt:message key="people" bundle="${myBundle}" />:</h4>
        </div>
        <div class="row">
            <div class="d-inline-flex justify-content-between">
                <div class="d-flex">
                    <input type="number" class="form-control me-1" style="width: 100px;" name="from" placeholder="<fmt:message key="from" bundle="${myBundle}" />" aria-label="From" min="0" max="${requestScope.peopleMaxCount}" value="${param.from}">
                    <input type="number" class="form-control ms-1" style="width: 100px;" name="to" placeholder="<fmt:message key="to" bundle="${myBundle}" />" aria-label="To" min="0" max="${requestScope.peopleMaxCount}" value="${param.to}">
                    <button type="submit" class="btn btn-primary ms-2"><fmt:message key="filter" bundle="${myBundle}" /></button>
                    <a href="<%= request.getContextPath() + "/a/activities" %>" class="btn btn-primary ms-1"><fmt:message key="reset" bundle="${myBundle}" /></a>
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
                            <option value=""><fmt:message key="sort_by" bundle="${myBundle}" /></option>
                            <c:choose>
                                <c:when test="${param.sort.equals('name')}">
                                    <option value="name" selected><fmt:message key="sort.alphabet" bundle="${myBundle}" /></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="name"><fmt:message key="sort.alphabet" bundle="${myBundle}" /></option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('create_time')}">
                                    <option value="create_time" selected><fmt:message key="sort.newest" bundle="${myBundle}" /></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="create_time"><fmt:message key="sort.newest" bundle="${myBundle}" /></option>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${param.sort.equals('people_count')}">
                                    <option value="people_count" selected><fmt:message key="sort.number_of_people" bundle="${myBundle}" /></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="people_count"><fmt:message key="sort.number_of_people" bundle="${myBundle}" /></option>
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
                                value="2">
                            <fmt:message key="sort" bundle="${myBundle}" />
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
                        <h5><a href="<%=request.getContextPath() + "/a/activity"%>?id=${activity.id}"
                               class="text-black text-decoration-none"><c:out value="${activity.name}" /></a></h5>
                        <h6 class="opacity-75">
                            <mtl:setCategoriesForActivities categoryList="${requestScope.categories}"
                                            activityCategoriesList="${activity.categories}" lang="${lang}" />
                        </h6>
                        <p class="card-text"><c:out value="${activity.description}" /></p>
                        <p class="card-text text-muted"><fmt:formatDate type="both" dateStyle="medium" value="${activity.createTime}" /></p>
                        <div class="d-flex justify-content-between align-items-center">
                            <div class="btn-group">
                                <a href="<%=request.getContextPath() + "/a/activity"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary"><fmt:message key="open" bundle="${myBundle}" /></a>
                                <a href="<%=request.getContextPath() + "/a/edit-act"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary"><fmt:message key="edit" bundle="${myBundle}" /></a>
                                <a href="<%=request.getContextPath() + "/a/delete-act"%>?id=${activity.id}"
                                   class="btn btn-sm btn-outline-secondary"><fmt:message key="remove" bundle="${myBundle}" /></a>
                            </div>

                            <div class="d-flex text-muted">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
                                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6z"/>
                                </svg>
                                <small><b><fmt:formatNumber type="number" value="${activity.peopleCount}" /></b></small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
