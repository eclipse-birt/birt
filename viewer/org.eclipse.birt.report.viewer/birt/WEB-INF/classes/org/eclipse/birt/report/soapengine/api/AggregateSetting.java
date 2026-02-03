package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Represents a setting for an aggregate in a BIRT report.
 */
@XmlRootElement(name = "AggregateSetting")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AggregateSetting", propOrder = { "enable", "showInFooter", "groupName" })
public class AggregateSetting implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "Enable")
	private Boolean enable;

	@XmlElement(name = "ShowInFooter")
	private Boolean showInFooter;

	@XmlElement(name = "GroupName")
	private String groupName;

	public AggregateSetting() {
	}

	public AggregateSetting(Boolean enable, Boolean showInFooter, String groupName) {
		this.enable = enable;
		this.showInFooter = showInFooter;
		this.groupName = groupName;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Boolean getShowInFooter() {
		return showInFooter;
	}

	public void setShowInFooter(Boolean showInFooter) {
		this.showInFooter = showInFooter;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof AggregateSetting))
			return false;
		AggregateSetting that = (AggregateSetting) o;
		return Objects.equals(enable, that.enable) && Objects.equals(showInFooter, that.showInFooter)
				&& Objects.equals(groupName, that.groupName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(enable, showInFooter, groupName);
	}

	@Override
	public String toString() {
		return "AggregateSetting{" + "enable=" + enable + ", showInFooter=" + showInFooter + ", groupName='" + groupName
				+ '\'' + '}';
	}
}
