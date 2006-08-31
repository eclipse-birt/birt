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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

public class LabelContent extends TextContent implements ILabelContent
{

	protected String helpTextKey;
	protected String labelText;
	protected String labelTextKey;

	public int getContentType( )
	{
		return LABEL_CONTENT;
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
		return helpText;
	}

	public void setHelpKey( String helpKey )
	{
		this.helpTextKey = helpKey;
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
	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitLabel( this, value );
	}

	static final protected short FIELD_HELPTEXTKEY = 600;
	static final protected short FIELD_LABELTEXT = 601;
	static final protected short FIELD_LABELTEXTKEY = 602;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( helpTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_HELPTEXTKEY );
			IOUtil.writeString( out, helpTextKey );
		}
		if ( labelText != null )
		{
			IOUtil.writeShort( out, FIELD_LABELTEXT );
			IOUtil.writeString( out, labelText );
		}
		if ( labelTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_LABELTEXTKEY );
			IOUtil.writeString( out, labelTextKey );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_HELPTEXTKEY :
				helpTextKey = IOUtil.readString( in );
				break;
			case FIELD_LABELTEXT :
				labelText = IOUtil.readString( in );
				break;
			case FIELD_LABELTEXTKEY :
				labelTextKey = IOUtil.readString( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}
}
