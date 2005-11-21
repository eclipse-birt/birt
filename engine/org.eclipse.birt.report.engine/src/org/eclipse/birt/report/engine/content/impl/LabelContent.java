/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content.impl;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

public class LabelContent extends TextContent implements ILabelContent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432225225256258289L;
	protected String helpText;
	protected String helpTextKey;
	protected String labelText;
	protected String labelTextKey;

	/**
	 * constructor. use by serialize and deserialize
	 */
	public LabelContent( )
	{

	}

	public LabelContent( ReportContent report )
	{
		super( report );
	}

	public LabelContent( IContent content )
	{
		super( content );
	}

	public void setHelpText( String helpText )
	{
		this.helpText = helpText;
	}

	public String getHelpText( )
	{
		if ( helpText == null )
		{
			if ( generateBy instanceof LabelItemDesign )
			{
				return ( (LabelItemDesign) generateBy ).getHelpText( );
			}
		}
		return null;
	}

	public void setHelpKey( String helpKey )
	{
		this.helpText = helpKey;
	}

	public String getHelpKey( )
	{
		if ( helpTextKey == null )
		{
			if ( generateBy instanceof LabelItemDesign )
			{
				return ( (LabelItemDesign) generateBy ).getHelpTextKey( );
			}
		}
		return helpTextKey;
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
