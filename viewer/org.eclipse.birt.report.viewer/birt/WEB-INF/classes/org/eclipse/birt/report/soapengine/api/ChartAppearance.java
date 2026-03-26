package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChartAppearance")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChartAppearance implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "ShowLegend")
	private Boolean showLegend;
	@XmlElement(name = "ShowValues")
	private Boolean showValues;
	@XmlElement(name = "Location")
	private ChartLocation location;
	@XmlElement(name = "Width")
	private String width;
	@XmlElement(name = "Height")
	private String height;

	public ChartAppearance() {
	}

	public ChartAppearance(Boolean showLegend, Boolean showValues, ChartLocation location, String width,
			String height) {
		this.showLegend = showLegend;
		this.showValues = showValues;
		this.location = location;
		this.width = width;
		this.height = height;
	}

	public Boolean getShowLegend() {
		return showLegend;
	}

	public void setShowLegend(Boolean showLegend) {
		this.showLegend = showLegend;
	}

	public Boolean getShowValues() {
		return showValues;
	}

	public void setShowValues(Boolean showValues) {
		this.showValues = showValues;
	}

	public ChartLocation getLocation() {
		return location;
	}

	public void setLocation(ChartLocation location) {
		this.location = location;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChartAppearance))
			return false;
		ChartAppearance that = (ChartAppearance) o;
		return Objects.equals(showLegend, that.showLegend) && Objects.equals(showValues, that.showValues)
				&& Objects.equals(location, that.location) && Objects.equals(width, that.width)
				&& Objects.equals(height, that.height);
	}

	@Override
	public int hashCode() {
		return Objects.hash(showLegend, showValues, location, width, height);
	}

	@Override
	public String toString() {
		return "ChartAppearance{" + "showLegend=" + showLegend + ", showValues=" + showValues + ", location=" + location
				+ ", width='" + width + '\'' + ", height='" + height + '\'' + '}';
	}
}
