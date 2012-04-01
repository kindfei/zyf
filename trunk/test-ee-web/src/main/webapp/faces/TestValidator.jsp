<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="faces-components" prefix="fs" %>

<f:view>
<f:loadBundle basename="messages" var="msg" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Validator</title>
</head>
<body>
	<h:messages showDetail="true" errorStyle="color: red" />
	
	<h:form id="form">
	<h:panelGrid columns="2">
		<h:panelGroup>
			<h:outputText value="ID" />
		</h:panelGroup>
		<h:inputText id="id" size="15" value="#{personBean.id}" required="true">
			<f:validateLength minimum="2" maximum="2" />
		</h:inputText>
		
		<h:panelGroup>
			<h:outputText value="NAME" />
		</h:panelGroup>
		<h:inputText id="name" value="#{personBean.name}">
			<f:validator validatorId="RegularExpressionValidator" />
		</h:inputText>
		
		<h:panelGroup>
			<h:outputText value="AGE"/>
		</h:panelGroup>
		<h:inputText id="age" value="#{personBean.age}">
			<fs:validateRegExp expression="^\d+$" errorMessage="It's not number" />
		</h:inputText>
	</h:panelGrid>
	<h:panelGrid columns="1">
		<h:commandButton id="result" type="submit" value="submit" action="#{personBean.result}" />
	</h:panelGrid>
	</h:form>
</body>
</html>
</f:view>