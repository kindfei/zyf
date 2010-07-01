	public String toString() {
		return ${pojo.importType("org.apache.commons.lang.builder.ReflectionToStringBuilder")}.toString(this, ${pojo.importType("org.apache.commons.lang.builder.ToStringStyle")}.SHORT_PREFIX_STYLE);
	}
