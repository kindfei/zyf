<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld" prefix="logic"%>

<html>
<head>
<title>finished register</title>
</head>
<body>

UserId:&nbsp&nbsp<bean:write name="RegisterForm" property="userId"/>
<br>
Email:&nbsp&nbsp&nbsp&nbsp<bean:write name="RegisterForm" property="email"/>
<br>
Address:&nbsp<bean:write name="RegisterForm" property="address"/>
<p>
register finished

</body>
</html>