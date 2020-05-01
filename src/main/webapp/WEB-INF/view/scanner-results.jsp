<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>URL Scanner Results</title>
    <%--@elvariable id="isDone" type="java.lang.Boolean"--%>
    <c:set var="isDone" scope="request" value="${isDone}"/>
    <script>
        let isDone = <c:out value="${isDone != null && isDone ? 'true' : 'false'}"/>;
        if (!isDone) {
            setTimeout(function () {
                window.location.href = "result"
            }, 1000);
        }
    </script>
</head>
<body>
<h2>URL Scanner Result</h2>
<c:out value="${isDone != null && isDone ? 'Checking finished.' : 'Checking in progress..' }"/>
<br><br>
<input type="button" value="Update" onclick="window.location.href='result'; return false;"/>
<input type="button" value="Back to start page" onclick="window.location.href='/'; return false;"/>
<br><br>
<div id="content">
    <table border="1" cellspacing="2" cellpadding="5">
        <tr>
            <th>URL</th>
            <th>STATUS</th>
            <th>TEXT FOUND</th>
            <th>ERROR MESSAGE</th>
        </tr>

        <%--@elvariable id="outputData" type="com.msyrovets.scanner.model.ScanOutputData"--%>
        <c:forEach var="outputData" items="${outputData}">

            <tr>
                <td>${outputData.url}</td>
                <td>${outputData.scanStatus}</td>
                <td>${outputData.isTargetTextFound}</td>
                <td>${outputData.errorMessage}</td>
            </tr>

        </c:forEach>
    </table>
</div>
</body>
</html>