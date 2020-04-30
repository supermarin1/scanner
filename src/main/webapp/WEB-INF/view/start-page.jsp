<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>URL Scanner</title>
</head>
<body>
<h2>URL Scanner</h2>
<%--@elvariable id="scanInputData" type="com.msyrovets.scanner.model.ScanInputData"--%>
<form:form action="result" modelAttribute="scanInputData">
    <br>
    URL: <form:input path="url"/>
    <br><br>
    Target text: <form:input path="targetText"/>
    <br><br>
    Max threads count: <form:input path="maxThreadsCount"/>
    <br><br>
    Max urls to scan: <form:input path="maxUrlsCount"/>
    <br><br>
    <input type="submit" value="Start scanning"/>
</form:form>
</body>
</html>