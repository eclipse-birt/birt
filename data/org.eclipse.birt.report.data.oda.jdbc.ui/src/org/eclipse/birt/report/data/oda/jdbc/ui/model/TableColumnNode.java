/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;


public class TableColumnNode implements IDBNode, Comparable<TableColumnNode>
{
	private static String COLUMN_ICON = TableColumnNode.class.getName( ) + ".ColumnIcon";
	static
	{
		ImageRegistry reg = JFaceResources.getImageRegistry( );
		reg.put( COLUMN_ICON,
				ImageDescriptor.createFromFile( JdbcPlugin.class, "icons/column.gif" ) );//$NON-NLS-1$
	}
	
	private String schemaName;
	private String tableName;
	private String columnName;
	private String typeName;
	
	public TableColumnNode( String schemaName, String tableName, String columnName,
			String typeName )
	{
		assert columnName != null && tableName != null;
		this.columnName = columnName;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.typeName = typeName;
	}

	public String getDisplayName( )
	{
		return columnName + " (" + typeName + ")";
	}

	public Image getImage( )
	{
		return JFaceResources.getImage( COLUMN_ICON );
	}

	public String getQualifiedNameInSQL( boolean useIdentifierQuoteString )
	{
		StringBuffer sb = new StringBuffer( );
		String quoteFlag = "";
		if ( useIdentifierQuoteString )
		{
			quoteFlag
				= JdbcMetaDataProvider.getInstance( ).getIdentifierQuoteString( );
		}
		if ( schemaName != null )
		{
			sb.append( Utility.quoteString( schemaName, quoteFlag ) ).append( "." );
		}
		sb.append( Utility.quoteString( tableName, quoteFlag ) ).append( "." );
		sb.append( Utility.quoteString( columnName, quoteFlag ) );
		return sb.toString( );
	}

	public int compareTo( TableColumnNode o )
	{
		/**
		 * In our case, 2 <code>TableColumn</code> instances need to be compared
		 * <p>only when they belong to the same <schema, table>
		 */
		return this.columnName.compareTo( o.columnName );
	}

}
