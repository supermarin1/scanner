<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>URL Scanner</title>

    <style>
        .error {color: red}
    </style>
</head>
<body>
<h2>URL Scanner</h2>
<%--@elvariable id="scanInputData" type="com.msyrovets.scanner.model.ScanInputData"--%>
<form:form action="result" modelAttribute="scanInputData">
    <br>
    URL: <form:input path="url"/>
    <form:errors path="url" cssClass="error" />
    <br><br>
    Target text: <form:input path="targetText"/>
    <form:errors path="targetText" cssClass="error" />
    <br><br>
    Max threads count: <form:input path="maxThreadsCount"/>
    <form:errors path="maxThreadsCount" cssClass="error" />
    <br><br>
    Max urls to scan: <form:input path="maxUrlsCount"/>
    <form:errors path="maxUrlsCount" cssClass="error" />
    <br><br>
    <input type="submit" value="Start scanning"/>
</form:form>
</body>
</html>