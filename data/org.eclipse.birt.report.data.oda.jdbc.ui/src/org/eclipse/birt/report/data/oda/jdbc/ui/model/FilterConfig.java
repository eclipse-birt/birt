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


public class FilterConfig
{
	public static enum Type{ ALL, TABLE, VIEW, PROCEDURE };
	
	private Type type;
	private boolean isShowSystemTable;
	private String schemaName; //null if no schema specified
	private String namePattern;
	
	private int maxSchemaCount;
	private int maxTableCountPerSchema;
	
	public FilterConfig( String schemaName, Type type, String namePattern,
			 boolean isShowSystemTable,
			 int maxSchemaCount, 
			 int maxTableCountPerSchema )
	{
		assert type != null && maxSchemaCount > 0 && maxTableCountPerSchema > 0;
		this.schemaName = schemaName;
		this.type = type;
		this.isShowSystemTable = isShowSystemTable;
		this.namePattern = generatePattern( namePattern );
		this.maxSchemaCount = maxSchemaCount;
		this.maxTableCountPerSchema = maxTableCountPerSchema;
	}

	public Type getType( )
	{
		return type;
	}
	
	public boolean isShowSystemTable( )
	{
		return isShowSystemTable;
	}
	
	public String getNamePattern( )
	{
		return namePattern;
	}
	
	public String getSchemaName( )
	{
		return schemaName;
	}

	
	public int getMaxSchemaCount( )
	{
		return maxSchemaCount;
	}

	
	public int getMaxTableCountPerSchema( )
	{
		return maxTableCountPerSchema;
	}
	
	/**
	 * @return null if no table/view needed to query
	 */
	public String[] getTableTypesForJDBC( )
	{
		switch ( type )
		{
			case PROCEDURE:
				return null;
			case TABLE:
				if ( isShowSystemTable( ) )
				{
					return new String[]{ "TABLE", "SYSTEM TABLE" };
				}
				else
				{
					return new String[]{ "TABLE" };
				}
			case VIEW:
				return new String[]{ "VIEW" };
			case ALL:
				if ( isShowSystemTable( ) )
				{
					return new String[]{ "TABLE", "VIEW", "SYSTEM TABLE" };
				}
				else
				{
					return new String[]{ "TABLE", "VIEW" };
				}
			default:
				//should never goes here
				assert false;
				return null;
		}
	}
	
	private static String generatePattern( String input )
	{
		if ( input != null )
		{
			if ( input.lastIndexOf( '%' ) == -1 )
			{
				input = input + "%";
			}
		}
		else
		{
			input = "%";
		}
		return input;
	}
	
	public static String getTypeDisplayText( Type type )
	{
		assert type != null;
		switch ( type )
		{
			case PROCEDURE:
				return JdbcPlugin.getResourceString( "tablepage.text.procedure" );
			case TABLE:
				return JdbcPlugin.getResourceString( "tablepage.text.tabletype" );
			case VIEW:	
				return JdbcPlugin.getResourceString( "tablepage.text.viewtype" );
			case ALL:
				return JdbcPlugin.getResourceString( "tablepage.text.All" );
			default:
				//should never goes here
				assert false;
				return "";
		}
	}
}
