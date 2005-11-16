
package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

public class LabelContent extends TextContent implements ILabelContent
{
	protected String labelText;
	protected String labelTextKey;

	public LabelContent( ReportContent report )
	{
		super( report );
	}

	public LabelContent( IContent content )
	{
		super( content );
	}

	public String getHelpText( )
	{
		if ( helpText != null )
		{
			return helpText;
		}

		if ( generateBy instanceof LabelItemDesign )
		{
			return ( (LabelItemDesign) generateBy ).getHelpText( );
		}
		
		return null;
	}

	public String getHelpKey( )
	{
		if ( generateBy instanceof LabelItemDesign )
		{
			return ( (LabelItemDesign) generateBy ).getHelpTextKey( );
		}
		return null;
	}

	public void setLabelText( String labelText )
	{
		this.labelText = labelText;
	}

	public String getLabelText( )
	{
		if ( labelText == null )
		{
			if ( generateBy instanceof LabelItemDesign )
			{
				return ( (LabelItemDesign) generateBy ).getText( );
			}
		}
		return labelText;
	}

	public void setLabelKey( String labelKey )
	{
		this.labelTextKey = labelKey;
	}

	public String getLabelKey( )
	{
		if ( labelTextKey == null )
		{
			if ( generateBy instanceof LabelItemDesign )
			{
				return ( (LabelItemDesign) generateBy ).getTextKey( );
			}
		}
		return labelTextKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitLabel( this, value );
	}
}
