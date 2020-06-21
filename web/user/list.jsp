<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="users?action=create">Add New User</a>
    </h2>
</center>

<center>
    <form action="users?action=find" method="post">
        <input type="text" name="findingUser" id="findingUser" size="45"/>
        <input type="submit" value="Find user by country">
    </form>
</center>

<center>
    <h2>
        <a href="users?action=sort">Sort User by name</a>
    </h2>
</center>

<center>
    <h2>
        <a href="users?action=permission">Add User permission</a>
    </h2>
</center>

<center>
    <h2>
        <a href="users?action=test-without-tran">Insert Update Without Transaction</a>
    </h2>
</center>

<center>
    <h2>
        <a href="users?action=test-use-tran">Insert Update With Transaction</a>
    </h2>
</center>

<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Users</h2></caption>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Country</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="user" items="${listUser}">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td><c:out value="${user.country}"/></td>
                <td>
                    <a href="users?action=edit&id=${user.id}">Edit</a>
                    <a href="users?action=delete&id=${user.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
