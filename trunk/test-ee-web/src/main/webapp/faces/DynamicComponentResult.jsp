<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="java.util.List"%>
<%@page import="test.faces.bean.model.User"%>
<%@page import="java.util.ArrayList"%>
<%@page import="javax.faces.application.Application"%>
<%@page import="javax.faces.context.FacesContext"%>
<%@page import="test.faces.bean.DynamicComponentBean"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="faces-components" prefix="fs" %>

<f:view>
	<html>
	<head>
	<title>Test Result</title>
	</head>
	<body>
	<h:form id="form">
		<%
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		DynamicComponentBean bean = (DynamicComponentBean) app.createValueBinding("#{dynaCompBean}").getValue(context);
		%>
		<p>
			<h:outputText value="Programmatically" style="font-family: Arial, sans-serif; font-size: 24; color: green;" />
		</p>
		<table>
			<c:forEach var="user" items="${dynaCompBean.users}">
				<tr>
					<td>
					<c:choose>
						<c:when test="${user.gender == 'male'}">
							<c:out value="Mr" />
						</c:when>
						<c:otherwise>
							<c:out value="Mrs" />
						</c:otherwise>
					</c:choose>
					</td>
					<td><c:out value="${user.name.firstName}" /></td>
					<td><c:out value="${user.name.lastName}" /></td>
				</tr>
			</c:forEach>
		</table>
		<p>
		<%
		out.print(bean.getUsers().toString());
		bean.getUsers().clear();
		%>
		</p>
		
		<p>
			<h:outputText value="Data Table" style="font-family: Arial, sans-serif; font-size: 24; color: green;" />
		</p>
		<h:dataTable id="dataTable" value="#{dynaCompBean.dataModel}" var="row">
			<h:column>
				<f:facet name="header">
					<h:outputText value="Name" />
				</f:facet>
				<h:outputText value="#{row.name}">
					<fs:nameConverter style="F_L" />
				</h:outputText>
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Gender" />
				</f:facet>
				<h:outputText value="#{row.gender}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText value="Age" />
				</f:facet>
				<h:outputText value="#{row.age}" />
			</h:column>
		</h:dataTable>
		<p>
		<%
		out.print(bean.getDataModel().getWrappedData().toString());
		((List<?>) bean.getDataModel().getWrappedData()).clear();
		%>
		</p>
		
		<h:panelGrid columns="1">
			<h:commandButton type="submit" value="Back" action="#{dynaCompBean.back}" />
		</h:panelGrid>
		
	</h:form>
	</body>
	</html>
</f:view>
