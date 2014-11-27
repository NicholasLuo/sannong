<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 11/22/14
  Time: 21:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Benefitting Agriculture - End User Profile</title>
</head>
<body>
    <h3>我的个人信息</h3>
    <form class="userForm" role="form" id="userInfoForm" action="updateUserInfo" method="post">
      <div class="row">
        <aside class="userFormCol-1">姓名</aside>
        <aside class="userFormCol-right">${myinfo.realName}</aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">职位</aside>
        <aside class="userFormCol-right"><input type="text" class="width-172" id="jobTitle" name="jobTitle" placeholder="职务"  value="${myinfo.jobTitle}"></aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">工作单位</aside>
        <aside class="userFormCol-right"><input type="text" class="width-281" id="jobCompany"  name="company" placeholder="工作单位"  value="${myinfo.company}"></aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">单位地址</aside>
        <aside class="userFormCol-right">
          <div class="width-87">
            <select>
              <option></option>
              <option>1</option>
              <option>2</option>
            </select>
          </div>
          <div class="width-87">
            <select>
              <option></option>
              <option>1</option>
              <option>2</option>
            </select>
          </div>
          <div class="width-87">
            <select>
              <option></option>
              <option>1</option>
              <option>2</option>
            </select>
          </div>
          <input type="text" class="width-273" id="jobAddress" name="companyAddress" placeholder="单位地址"  value="${myinfo.companyAddress}">
        </aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">工作电话</aside>
        <aside class="userFormCol-right"><input type="text" class="width-281" id="deskPhone" name="deskPhone"  placeholder="工作电话"  value="${myinfo.deskPhone}"></aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">电子邮件</aside>
        <aside class="userFormCol-right"><input type="text" class="width-281" id="mailbox"  name="mailbox" placeholder="电子邮箱"  value="${myinfo.mailbox}"></aside>
      </div>
      <div class="row">
        <aside class="userFormCol-1">手机号码</aside>
        <aside class="userFormCol-right">
          <span class="font-size-20">0800 556 2540 ${myinfo.cellphone}</span>
          <sec:authorize access="hasRole('ROLE_USER') and isAuthenticated()">
            <a href="#" class="white-bt">更新手机号码</a>
          </sec:authorize>
        </aside>
      </div>
      <div class="row">
        <input type="submit" id="userInfoSubmit" value="提交"/>
      </div>
    </form>
</body>
</html>