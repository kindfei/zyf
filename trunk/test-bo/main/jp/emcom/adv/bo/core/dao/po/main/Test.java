package jp.emcom.adv.bo.core.dao.po.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import jp.emcom.adv.bo.core.dao.po.AbstractPersistentObject;
import jp.emcom.adv.bo.core.dao.po.PersistentObjects;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "TEST")
public class Test extends AbstractPersistentObject {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Version
	@Column(name = "VERSION")
	private Integer version;

	@Column(name = "CONTENT")
	private String content;

	/**
	 * constructor
	 */
	public Test() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Test copy() {
		return (Test) PersistentObjects.copy(this);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
