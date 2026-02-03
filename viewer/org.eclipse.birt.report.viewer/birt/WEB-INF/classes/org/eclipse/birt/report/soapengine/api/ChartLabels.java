package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChartLabels")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChartLabels implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Title")
	private String title;
	@XmlElement(name = "XAxis")
	private String XAxis;
	@XmlElement(name = "YAxis")
	private String[] YAxis;

	public ChartLabels() {
	}

	public ChartLabels(String title, String XAxis, String[] YAxis) {
		this.title = title;
		this.XAxis = XAxis;
		this.YAxis = YAxis;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getXAxis() {
		return XAxis;
	}

	public void setXAxis(String XAxis) {
		this.XAxis = XAxis;
	}

	public String[] getYAxis() {
		return YAxis;
	}

	public void setYAxis(String[] YAxis) {
		this.YAxis = YAxis;
	}

	public String getYAxis(int index) {
		return YAxis[index];
	}

	public void setYAxis(int index, String value) {
		YAxis[index] = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChartLabels))
			return false;
		ChartLabels that = (ChartLabels) o;
		return Objects.equals(title, that.title) && Objects.equals(XAxis, that.XAxis)
				&& Arrays.equals(YAxis, that.YAxis);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(title, XAxis);
		result = 31 * result + Arrays.hashCode(YAxis);
		return result;
	}

	@Override
	public String toString() {
		return "ChartLabels{" + "title='" + title + '\'' + ", XAxis='" + XAxis + '\'' + ", YAxis="
				+ Arrays.toString(YAxis) + '}';
	}
}
