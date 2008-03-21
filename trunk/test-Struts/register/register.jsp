<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<title>register</title>
</head>
<body>

<html:form action="/register.do" method="POST">
	UserId:&nbsp&nbsp<html:text property="userId" size="16" maxlength="16"/>
	<br>
	Email:&nbsp&nbsp&nbsp&nbsp<html:text property="email" size="16" maxlength="16"/>
	<br>
	Address:&nbsp<html:text property="address" size="16" maxlength="16"/>
	<p>
	<html:submit property="submit" value="Submit"/>
	<html:reset/>
</html:form>

</body>
</html>
