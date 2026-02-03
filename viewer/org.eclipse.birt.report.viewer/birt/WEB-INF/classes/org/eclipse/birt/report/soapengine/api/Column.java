package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Represents a report column definition in BIRT.
 */
@XmlRootElement(name = "Column")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Column", propOrder = { "name", "label", "visibility" })
public class Column implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Name", required = true)
	private String name;

	@XmlElement(name = "Label")
	private String label;

	@XmlElement(name = "Visibility")
	private Boolean visibility;

	public Column() {
	}

	public Column(String name, String label, Boolean visibility) {
		this.name = name;
		this.label = label;
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(Boolean visibility) {
		this.visibility = visibility;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Column))
			return false;
		Column column = (Column) o;
		return Objects.equals(name, column.name) && Objects.equals(label, column.label)
				&& Objects.equals(visibility, column.visibility);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, label, visibility);
	}

	@Override
	public String toString() {
		return "Column{" + "name='" + name + '\'' + ", label='" + label + '\'' + ", visibility=" + visibility + '}';
	}
}
