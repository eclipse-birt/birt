package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChartProperties")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChartProperties implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Type")
	private ChartType type;
	@XmlElement(name = "DataBinding")
	private ChartDataBinding dataBinding;
	@XmlElement(name = "Labels")
	private ChartLabels labels;
	@XmlElement(name = "Appearance")
	private ChartAppearance appearance;

	public ChartProperties() {
	}

	public ChartProperties(ChartType type, ChartDataBinding dataBinding, ChartLabels labels,
			ChartAppearance appearance) {
		this.type = type;
		this.dataBinding = dataBinding;
		this.labels = labels;
		this.appearance = appearance;
	}

	public ChartType getType() {
		return type;
	}

	public void setType(ChartType type) {
		this.type = type;
	}

	public ChartDataBinding getDataBinding() {
		return dataBinding;
	}

	public void setDataBinding(ChartDataBinding dataBinding) {
		this.dataBinding = dataBinding;
	}

	public ChartLabels getLabels() {
		return labels;
	}

	public void setLabels(ChartLabels labels) {
		this.labels = labels;
	}

	public ChartAppearance getAppearance() {
		return appearance;
	}

	public void setAppearance(ChartAppearance appearance) {
		this.appearance = appearance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChartProperties))
			return false;
		ChartProperties that = (ChartProperties) o;
		return Objects.equals(type, that.type) && Objects.equals(dataBinding, that.dataBinding)
				&& Objects.equals(labels, that.labels) && Objects.equals(appearance, that.appearance);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, dataBinding, labels, appearance);
	}

	@Override
	public String toString() {
		return "ChartProperties{" + "type=" + type + ", dataBinding=" + dataBinding + ", labels=" + labels
				+ ", appearance=" + appearance + '}';
	}
}
