package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DataField")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataField implements Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Name")
	private String name;
	@XmlElement(name = "Id")
	private String id;
	@XmlElement(name = "Type")
	private String type;
	@XmlElement(name = "DisplayName")
	private String displayName;
	@XmlElement(name = "Description")
	private String description;

	public DataField() {
	}

	public DataField(String name, String id, String type, String displayName, String description) {
		this.name = name;
		this.id = id;
		this.type = type;
		this.displayName = displayName;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DataField))
			return false;
		DataField that = (DataField) o;
		return Objects.equals(name, that.name) && Objects.equals(id, that.id) && Objects.equals(type, that.type)
				&& Objects.equals(displayName, that.displayName) && Objects.equals(description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, id, type, displayName, description);
	}

}
