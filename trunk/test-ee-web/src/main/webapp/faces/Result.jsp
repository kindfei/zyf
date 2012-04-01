<%@page import="javax.faces.context.ExternalContext"%>
<%@page import="javax.faces.application.Application"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="faces-components" prefix="fs" %>

<f:view>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>To String</title>
</head>
<body>
	<%
	FacesContext context = FacesContext.getCurrentInstance();
	Application app = context.getApplication();
	out.println(app.createValueBinding("#{" + session.getAttribute("beanName") + "}").getValue(context));
	%>
</body>
</html>
</f:view>