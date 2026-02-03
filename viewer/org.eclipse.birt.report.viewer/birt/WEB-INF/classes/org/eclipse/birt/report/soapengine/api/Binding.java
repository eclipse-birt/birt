package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Binding")
@XmlAccessorType(XmlAccessType.FIELD)
public class Binding {
	@XmlElement(name = "RptElementId")
	private long rptElementId;
	@XmlElement(name = "DataSetId")
	private long dataSetId;

	public Binding() {
	}

	public Binding(long rptElementId, long dataSetId) {
		this.rptElementId = rptElementId;
		this.dataSetId = dataSetId;
	}

	public long getRptElementId() {
		return rptElementId;
	}

	public void setRptElementId(long rptElementId) {
		this.rptElementId = rptElementId;
	}

	public long getDataSetId() {
		return dataSetId;
	}

	public void setDataSetId(long dataSetId) {
		this.dataSetId = dataSetId;
	}
}
