<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        TABLE, TD, TH {
            border: 1px solid black; /* Рамка вокруг таблицы */
        }
        tr .excess {
            color: red;
            border: 1px solid grey;
        }
        tr.notexcess {
            color: green;
            border: 1px solid grey;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${MealsList}">
        <tr class="${meal.excess ? 'excess' : 'notexcess'}">
            <td>${meal.getDateTime().toString().replace('T', ' ')}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td>${meal.isExcess()}</td>
            <td></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
