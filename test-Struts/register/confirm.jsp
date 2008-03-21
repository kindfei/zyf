<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<title>show infomation</title>
</head>
<body>

UserId:&nbsp&nbsp<bean:write name="RegisterForm" property="userId"/>
<br>
Email:&nbsp&nbsp&nbsp&nbsp<bean:write name="RegisterForm" property="email"/>
<br>
Address:&nbsp<bean:write name="RegisterForm" property="address"/>

</body>
</html>