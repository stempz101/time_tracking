<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ attribute
        name="pages"
        type="java.lang.Integer"
        required="true"
%>
<fmt:setLocale value="${sessionScope.lang}" />
<fmt:setBundle var="myBundle" basename="content" />
<c:if test="${pages > 1}">
    <%--                        Previous Button --%>
    <c:choose>
        <c:when test="${param.page == null || param.page == 1}">
            <li class="page-item disabled">
                <span class="page-link"><fmt:message key="previous" bundle="${myBundle}" /></span>
            </li>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${pageContext.request.queryString != null}">
                    <a class="page-link" href="${requestScope.queryForPagination}page=${requestScope.previousPage}"><fmt:message key="previous" bundle="${myBundle}" /></a>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=${requestScope.previousPage}"><fmt:message key="previous" bundle="${myBundle}" /></a>
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
                            <a class="page-link" href="${requestScope.queryForPagination}page=${i}">${i}</a>
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
                <a class="page-link" href="#"><fmt:message key="next" bundle="${myBundle}" /></a>
            </li>
        </c:when>
        <c:otherwise>
            <c:choose>
                <c:when test="${pageContext.request.queryString != null}">
                    <a class="page-link" href="${requestScope.queryForPagination}page=${requestScope.nextPage}"><fmt:message key="next" bundle="${myBundle}" /></a>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=${requestScope.nextPage}"><fmt:message key="next" bundle="${myBundle}" /></a>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</c:if>
