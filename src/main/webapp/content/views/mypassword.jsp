<%--
  Created by IntelliJ IDEA.
  User: Bright Huang
  Date: 10/14/14
  Time: 10:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <link href="content/static/css/bootstrap-3.2.0/bootstrap.css" rel="stylesheet">
    <title></title>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <jsp:include page='navbar.jsp'/>
        </div>
    </div>
    <div class="row clearfix">
        <div class="col-md-4 column">
        </div>
        <div class="col-md-4 column">
            <form role="formPassword" action="updatepassword" method="post">
                <div>
                    <div class="form-group">
                        <label for="oldPassword">旧密码</label>
                        <input type="password" class="form-control" id="oldPassword" name="oldPassword" placeholder="旧密码">
                    </div>
                    <div class="form-group">
                        <label for="newPassword">新密码</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" placeholder="新密码">
                    </div>
                    <div class="form-group">
                        <label for="confirmedPassword">确认新密码</label>
                        <input type="password" class="form-control" id="confirmedPassword" name="confirmedPassword" placeholder="确认新密码">
                    </div>
                </div>
                <button type="submit" class="btn btn-success">Submit</button>
            </form>
            <c:choose>
                <c:when test="${myPasswordAuth == 'oldPasswordAuthFailure'}">
                    <div id="authMsg1"  style="display: block;color: red">
                        旧密码不匹配, 请重新输入.
                    </div>
                </c:when>
                <c:when test="${myPasswordAuth == 'newPasswordAuthFailure'}">
                    <div id="authMsg2"  style="display: block;color: red">
                        新密码与确认密码不匹配, 请重新输入.
                    </div>
                </c:when>
                <c:when test="${myPasswordAuth == 'passwordChanged'}">
                    <div id="authMsg2"  style="display: block;color: blue">
                        密码修改成功.
                    </div>
                </c:when>

                <c:otherwise>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="col-md-4 column">
        </div>
    </div>
</div>
</body>
</html>
