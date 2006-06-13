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
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.13 $ $Date: 2006/05/18 09:10:25 $
 */
public class RowContent extends AbstractContent implements IRowContent
{
	
	protected int rowID = -1;	
	
	protected boolean isStartOfGroup = false;

	public int getContentType( )
	{
		return ROW_CONTENT;
	}

	/**
	 * constructor
	 * 
	 * @param row
	 *            the row deign
	 */
	public RowContent( IReportContent report )
	{
		super( report );
	}

	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitRow( this, value );
	}

	public int getRowID( )
	{
		return rowID;
	}
	
	public void setRowID(int rowID)
	{
		this.rowID = rowID;
	}
	
	public ITableContent getTable( )
	{
		IContent parent = (IContent) getParent( );
		while ( parent != null )
		{
			if ( parent.getContentType( ) == IContent.TABLE_CONTENT )
			{
				return (ITableContent) parent;
			}
			parent = (IContent) parent.getParent( );
		}
		return null;
	}

	static final protected int FIELD_ROWID = 800;
	static final protected int FIELD_ROWTYPE = 801;
	static final protected int FIELD_ROW_GROUPLEVEL = 802;
	static final protected int FIELD_ROW_GROUPID = 803;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( rowID != -1 )
		{
			IOUtil.writeInt( out,  FIELD_ROWID );
			IOUtil.writeInt( out,  rowID );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_ROWID :
				rowID = IOUtil.readInt(in);
				break;
			case FIELD_ROWTYPE :
				IOUtil.readInt(in);
				break;
			case FIELD_ROW_GROUPLEVEL :
				IOUtil.readInt(in);
				break;
			case FIELD_ROW_GROUPID :
				IOUtil.readString( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}
}