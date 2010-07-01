<#-- // Fields -->

<#foreach field in pojo.getAllPropertiesIterator()>
<#if pojo.getMetaAttribAsBool(field, "gen-property", true)>
<#if pojo.hasMetaAttribute(field, "field-description")>
	/**
	 ${pojo.getFieldJavaDoc(field, 0)}
	 */
</#if>
<#if field.name!='inputStaffId' && field.name!='inputDate' && field.name!='updateStaffId' && field.name!='updateDate'>
	<#include "Ejb3FieldAnnotation.ftl"/>
	${pojo.getFieldModifiers(field)} ${pojo.getJavaTypeName(field, jdk5)} ${field.name}<#if pojo.hasFieldInitializor(field, jdk5)> = ${pojo.getFieldInitialization(field, jdk5)}</#if>;
</#if>
</#if>
</#foreach>
