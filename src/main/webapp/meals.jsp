<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>
    <table border="1">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach items="${mealsTo}" var="meal">
            <tr style="color:${meal.getExceed() ? 'red' : 'green'}">
                <td>${meal.getDescription()}</td>
                <td>${meal.getDateTime()}</td>
                <td>${meal.getCalories()}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
