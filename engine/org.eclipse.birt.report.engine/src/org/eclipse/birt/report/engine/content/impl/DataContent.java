
package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

public class DataContent extends TextContent implements IDataContent
{

	protected Object value;

	protected String labelText;

	protected String labelKey;

	public DataContent( ReportContent report )
	{
		super( report );
	}

	public DataContent( IContent content )
	{
		super( content );
	}

	public Object getValue( )
	{
		return value;
	}

	public void setValue( Object value )
	{
		this.value = value;
	}

	public String getLabelText( )
	{
		return labelText;
	}

	public void setLabelText( String text )
	{
		this.labelText = text;
	}

	public String getLabelKey( )
	{
		return this.labelKey;
	}

	public void setLabelKey( String key )
	{
		this.labelKey = key;
	}

	public String getHelpText( )
	{
		if ( generateBy instanceof DataItemDesign )
		{
			return ( (DataItemDesign) generateBy ).getHelpText( );
		}
		return null;
	}

	public String getHelpKey( )
	{
		if ( generateBy instanceof DataItemDesign )
		{
			return ( (DataItemDesign) generateBy ).getHelpTextKey( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitData( this, value );
	}

}
