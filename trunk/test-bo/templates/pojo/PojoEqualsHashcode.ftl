<#if pojo.needsEqualsHashCode() && !clazz.superclass?exists>
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		${pojo.getDeclarationName()} other = (${pojo.getDeclarationName()}) obj;
<#foreach property in pojo.getAllPropertiesIterator()>
		if (${property.name} == null) {
			if (other.${property.name} != null)
				return false;
		} else if (!${property.name}.equals(other.${property.name}))
			return false;
</#foreach>
		return true;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
<#foreach property in pojo.getAllPropertiesIterator()>
		result = prime * result + ((${property.name} == null) ? 0 : ${property.name}.hashCode());
</#foreach>
		return result;
	}
</#if>