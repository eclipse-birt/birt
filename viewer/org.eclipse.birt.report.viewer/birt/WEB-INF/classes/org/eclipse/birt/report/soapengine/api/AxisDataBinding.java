package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "AxisDataBinding")
@XmlAccessorType(XmlAccessType.FIELD)
public class AxisDataBinding {
	@XmlElement(name = "ColumnData")
	private ColumnDefinition columnData;
	@XmlElement(name = "SectionData")
	private SectionDefinition sectionData;

	public AxisDataBinding() {
	}

	public AxisDataBinding(ColumnDefinition columnData, SectionDefinition sectionData) {
		this.columnData = columnData;
		this.sectionData = sectionData;
	}

	public ColumnDefinition getColumnData() {
		return columnData;
	}

	public void setColumnData(ColumnDefinition columnData) {
		this.columnData = columnData;
	}

	public SectionDefinition getSectionData() {
		return sectionData;
	}

	public void setSectionData(SectionDefinition sectionData) {
		this.sectionData = sectionData;
	}
}
