package org.eclipse.birt.report.engine.api;


public class DataID
{
	protected String dataSetName;
	protected long rowId;
	
	public DataID(String dataSetName, long rowId)
	{
		this.dataSetName = dataSetName;
		this.rowId = rowId;
	}
	
	public String toString()
	{
		return dataSetName + ":" + rowId;
	}
}
