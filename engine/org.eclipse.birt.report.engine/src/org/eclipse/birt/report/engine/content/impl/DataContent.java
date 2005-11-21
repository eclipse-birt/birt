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
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.ir.DataItemDesign;

public class DataContent extends TextContent implements IDataContent
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6575007426044738170L;

	protected Object value;

	protected String labelText;

	protected String labelKey;

	protected String helpText;

	protected String helpKey;

	/**
	 * constructor use by serialize and deserialize
	 */
	public DataContent( )
	{

	}

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
		if ( helpText == null )
		{
			if ( generateBy instanceof DataItemDesign )
			{
				return ( (DataItemDesign) generateBy ).getHelpText( );
			}
		}
		return helpText;
	}

	public String getHelpKey( )
	{
		if ( helpKey == null )
		{
			if ( generateBy instanceof DataItemDesign )
			{
				return ( (DataItemDesign) generateBy ).getHelpTextKey( );
			}
		}
		return helpKey;
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
