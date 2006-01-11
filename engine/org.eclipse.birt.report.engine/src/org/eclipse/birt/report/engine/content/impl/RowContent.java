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
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IRowContent;

/**
 * 
 * the row content object which contains cell content objects
 * 
 * @version $Revision: 1.9 $ $Date: 2005/12/07 07:21:33 $
 */
public class RowContent extends AbstractContent implements IRowContent
{
	
	protected int rowID = -1;	

	/**
	 * constructor. use by serialize and deserialize
	 */
	public RowContent( )
	{

	}

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
	public RowContent( ReportContent report )
	{
		super( report );
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitRow( this, value );
	}

	public int getRowID( )
	{
		return rowID;
	}
	
	public void setRowID(int rowID)
	{
		this.rowID = rowID;
	}	

	static final protected int FIELD_ROWID = 800;
	

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
			default :
				super.readField( version, filedId, in );
		}
	}
}