package org.eclipse.birt.report.soapengine.api;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ChartDataBinding")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChartDataBinding implements Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "TableId")
	private String tableId;
	@XmlElement(name = "Section")
	private SectionDefinition section;
	@XmlElement(name = "XAxis")
	private AxisDataBinding XAxis;
	@XmlElement(name = "YAxis")
	private AxisDataBinding[] YAxis;
	@XmlElement(name = "ShowXLabelsVertically")
	private int showXLabelsVertically;

	public ChartDataBinding() {
	}

	public ChartDataBinding(String tableId, SectionDefinition section, AxisDataBinding XAxis, AxisDataBinding[] YAxis,
			int showXLabelsVertically) {
		this.tableId = tableId;
		this.section = section;
		this.XAxis = XAxis;
		this.YAxis = YAxis;
		this.showXLabelsVertically = showXLabelsVertically;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public SectionDefinition getSection() {
		return section;
	}

	public void setSection(SectionDefinition section) {
		this.section = section;
	}

	public AxisDataBinding getXAxis() {
		return XAxis;
	}

	public void setXAxis(AxisDataBinding XAxis) {
		this.XAxis = XAxis;
	}

	public AxisDataBinding[] getYAxis() {
		return YAxis;
	}

	public void setYAxis(AxisDataBinding[] YAxis) {
		this.YAxis = YAxis;
	}

	public AxisDataBinding getYAxis(int index) {
		return YAxis[index];
	}

	public void setYAxis(int index, AxisDataBinding value) {
		YAxis[index] = value;
	}

	public int getShowXLabelsVertically() {
		return showXLabelsVertically;
	}

	public void setShowXLabelsVertically(int showXLabelsVertically) {
		this.showXLabelsVertically = showXLabelsVertically;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ChartDataBinding))
			return false;
		ChartDataBinding that = (ChartDataBinding) o;
		return showXLabelsVertically == that.showXLabelsVertically && Objects.equals(tableId, that.tableId)
				&& Objects.equals(section, that.section) && Objects.equals(XAxis, that.XAxis)
				&& Arrays.equals(YAxis, that.YAxis);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(tableId, section, XAxis, showXLabelsVertically);
		result = 31 * result + Arrays.hashCode(YAxis);
		return result;
	}

	@Override
	public String toString() {
		return "ChartDataBinding{" + "tableId='" + tableId + '\'' + ", section=" + section + ", XAxis=" + XAxis
				+ ", YAxis=" + Arrays.toString(YAxis) + ", showXLabelsVertically=" + showXLabelsVertically + '}';
	}
}
