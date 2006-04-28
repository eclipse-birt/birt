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
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;

public class AutoTextContent extends TextContent implements IAutoTextContent
{
	protected int type = -1;

	public int getContentType( )
	{
		return AUTOTEXT_CONTENT;
	}

	public AutoTextContent( ReportContent report )
	{
		super( report );
	}

	public AutoTextContent( IContent content )
	{
		super( content );
	}
	
	public void setType ( int type )
	{
		this.type  = type;
	}

	public int getType ( )
	{
		return this.type;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.impl.AbstractContent#accept(org.eclipse.birt.report.engine.content.IContentVisitor)
	 */
	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitAutoText( this, value );
	}
	
	static final protected int FIELD_TYPE = 650;
	static final protected int FIELD_TEXT = 651;
	
	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( type != -1 )
		{
			IOUtil.writeInt( out, FIELD_TYPE );
			IOUtil.writeInt( out, type );
		}
		if ( text != null)
		{
			IOUtil.writeInt( out, FIELD_TEXT );
			IOUtil.writeString( out, text );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case  FIELD_TYPE:
				type = IOUtil.readInt( in );
				break;
			case  FIELD_TEXT:
				text = IOUtil.readString( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}
