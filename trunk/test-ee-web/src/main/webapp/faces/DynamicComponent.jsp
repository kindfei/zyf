<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="faces-components" prefix="fs" %>

<script type="text/javascript">
<!--

//-->
</script>

<f:view>
	<f:loadBundle basename="messages" var="msg" />
	<html>
	<head>
	<title>Dynamic Component</title>
	</head>
	
	<script type="text/javascript">
		window.history.forward(1);
	</script>
	
	<body>
	
	<h:form id="from" onsubmit="">
		<h:messages showDetail="true" errorStyle="color: red" />
		
		<h:panelGrid columns="1">
			<h:outputText value="Programmatically" style="font-family: Arial, sans-serif; font-size: 24; color: green;" />
			
			<h:panelGrid id="controlPanel" binding="#{dynaCompBean.controlPanel}" columns="1" border="0" cellspacing="0" cellpadding="0" >
			</h:panelGrid>
			
			<h:commandButton type="submit" value="Add" actionListener="#{dynaCompBean.addProgrammatically}" immediate="true" />
		</h:panelGrid>
		
		<h:panelGrid columns="1">
			<h:outputText value="Data Table" style="font-family: Arial, sans-serif; font-size: 24; color: green;" />
			
			<h:dataTable id="dataTable" value="#{dynaCompBean.dataModel}" var="row" rendered="#{dynaCompBean.dataModel.rowCount > 0}">
				<h:column>
					<f:facet name="header">
						<h:outputText value="Name" />
					</f:facet>
					<h:inputText value="#{row.name}">
						<fs:nameConverter style="L_F" />
					</h:inputText>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Gender" />
					</f:facet>
					<h:selectOneMenu value="#{row.gender}">
						<f:selectItem itemValue="male" itemLabel="male" />
						<f:selectItem itemValue="female" itemLabel="female" />
					</h:selectOneMenu>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText value="Age" />
					</f:facet>
					<h:inputText value="#{row.age}">
					</h:inputText>
				</h:column>
				<h:column>
					<h:commandButton value="Remove" actionListener="#{dynaCompBean.removeDataTableRow}" />
				</h:column>
			</h:dataTable>
			
			<h:commandButton type="submit" value="Add" actionListener="#{dynaCompBean.addDataTableRow}" />
		</h:panelGrid>
		
		<h:panelGrid columns="1">
			<h:commandButton id="result" type="submit" value="Result" action="#{dynaCompBean.result}" />
		</h:panelGrid>
	</h:form>
	
	</body>
	</html>
</f:view>
