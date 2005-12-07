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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;

public class LabelContent extends TextContent implements ILabelContent
{
	protected String helpTextKey;
	protected String labelText;
	protected String labelTextKey;

	/**
	 * constructor. use by serialize and deserialize
	 */
	public LabelContent( )
	{

	}

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
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitLabel( this, value );
	}

	static final protected int FIELD_HELPTEXTKEY = 600;
	static final protected int FIELD_LABELTEXT = 601;
	static final protected int FIELD_LABELTEXTKEY = 602;

	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( helpTextKey != null )
		{
			out.writeInt( FIELD_HELPTEXTKEY );
			out.writeUTF( helpTextKey );
		}
		if ( labelText != null )
		{
			out.writeInt( FIELD_LABELTEXT );
			out.writeUTF( labelText );
		}
		if ( labelTextKey != null )
		{
			out.writeInt( FIELD_LABELTEXTKEY );
			out.writeUTF( labelTextKey );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_HELPTEXTKEY :
				helpTextKey = in.readUTF( );
				break;
			case FIELD_LABELTEXT :
				labelText = in.readUTF( );
				break;
			case FIELD_LABELTEXTKEY :
				labelTextKey = in.readUTF( );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}
}
