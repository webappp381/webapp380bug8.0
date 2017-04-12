<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>All Post</h2>

        <security:authorize access="hasRole('ADMIN')">    
            <a href="<c:url value="/user" />">Manage User Accounts</a><br /><br />
            
        </security:authorize>
        <security:authorize access="hasRole('ADMIN')"> 
        <a href="<c:url value="/poll/create" />">Create new poll</a>
        </security:authorize>
        <security:authorize access="isAuthenticated()">
        <a href="<c:url value="/ticket/create" />">New Post</a>  
        </security:authorize>        
        <a href="<c:url value="/ticket/listlecture" />">lecture</a>
        <a href="<c:url value="/ticket/listlab" />">lab</a>
        <a href="<c:url value="/ticket/listother" />">other</a>
        <a href="<c:url value="/login" />">login</a>
        <br /><br />
        <c:choose>
            <c:when test="${fn:length(ticketDatabase) == 0}">
                <i>0 Post.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ticketDatabase}" var="entry">
                    Post #${entry.id}:
                    <a href="<c:url value="/ticket/view/${entry.id}" />">
                        <c:out value="${entry.subject}" /></a>
                    (Post by: <c:out value="${entry.customerName}" />)            
                    <security:authorize access="isAuthenticated()">
                    <security:authorize access="hasRole('ADMIN') or principal.username=='${entry.customerName}'">            
                        [<a href="<c:url value="/ticket/edit/${entry.id}" />">Edit</a>]
                    </security:authorize>
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">            
                        [<a href="<c:url value="/ticket/delete/${entry.id}" />">Delete</a>]
                    </security:authorize>
                    <hr>    
                </c:forEach>
            </c:otherwise>
        </c:choose>
        
        <br>      
        <c:choose>
            <c:when test="${fn:length(pollTicketDatabase) == 0}">
                <i>0 Poll.</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${pollTicketDatabase}" var="entry5">
                    Poll #${entry5.key}: <a href="<c:url value="/poll/view/${entry5.key}" />"> 
                        <c:out value="${entry5.value.pollSubject}" /></a>
                    <br>
                </c:forEach>
            </c:otherwise>  
        </c:choose>

    </body>
</html>