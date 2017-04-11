
<!DOCTYPE html>
<html>
    <head>
        <title>poll Page</title>
    </head>
    <body>
        <h2>Poll #${pollId}: <c:out value="${poll.pollSubject}" /></h2>
        <select name="item" form="f">
            <c:forEach items="${poll.map}" var="entry">
                <option value ="${entry.key}"> ${entry.key} </option>
            </c:forEach>
        </select>
        <form method="POST" action="dopoll" id="f">  
            <input type="hidden" name="pollId" value="${pollId}"/>
            <input type="submit" value="Submit poll"/><br>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </body>
</html>
