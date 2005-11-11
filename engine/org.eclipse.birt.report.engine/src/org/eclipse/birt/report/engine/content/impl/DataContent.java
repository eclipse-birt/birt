package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

public class DataContent extends TextContent implements IDataContent {
	
	Object value;
	
	public DataContent(ReportContent report)
	{
		super(report);
	}
	
	public DataContent(IContent content)
	{
		super(content);
	}
	
	public Object getValue()
	{
		return value;
	}
	
	public void setValue(Object value)
	{
		this.value = value;
	}
	
	public String getHelpText()
	{
		if (generateBy instanceof DataItemDesign)
		{
			return ((DataItemDesign)generateBy).getHelpText();
		}
		return null;
	}
	
	public String getHelpKey()
	{
		if (generateBy instanceof DataItemDesign)
		{
			return ((DataItemDesign)generateBy).getHelpTextKey();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor , Object value)
	{
		visitor.visitData(this, value);
	}

}
