<#include "Ejb3TypeDeclaration.ftl"/>
${pojo.getClassModifiers()} ${pojo.getDeclarationType()} ${pojo.getDeclarationName()}
<#if pojo.isComponent()>
implements ${pojo.importType("jp.emcom.adv.bo.core.dao.po.PersistentId")}
<#else>
extends ${pojo.importType("jp.emcom.adv.bo.core.dao.po.AbstractPersistentObject")}
</#if>
