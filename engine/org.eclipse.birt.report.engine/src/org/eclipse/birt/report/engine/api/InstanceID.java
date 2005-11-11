package org.eclipse.birt.report.engine.api;


/**
 * a class that wraps around an identifier for a report element instance
 */
public class InstanceID
{
	InstanceID parentId;
	long designId;
	DataID dataId;

	public InstanceID(InstanceID parent, long designId, DataID dataId)
	{
		this.parentId = parent;
		this.designId = designId;
		this.dataId = dataId;
	}
	
	public InstanceID getParentID()
	{
		return parentId;
	}
	/**
	 * returns the component id for the element
	 */
	public long getComponentID()
	{
		return designId;
	}
	
	public DataID getDataID()
	{
		return dataId;
	}
	
	protected void append(StringBuffer buffer)
	{
		if (parentId != null)
		{
			parentId.append(buffer);
		}
		buffer.append("/");
		buffer.append(designId);
		buffer.append("(");
		buffer.append(dataId.dataSetName);
		buffer.append(":");
		buffer.append(dataId.rowId);
		buffer.append(")");
	}
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		append(buffer);
		return buffer.toString();
	}
}
