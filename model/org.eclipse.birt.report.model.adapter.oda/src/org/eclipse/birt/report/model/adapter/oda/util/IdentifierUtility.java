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

package org.eclipse.birt.report.model.adapter.oda.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;

/**
 * The utility class to create a unique name for result set column.
 * 
 */
public class IdentifierUtility
{

	private static final char RENAME_SEPARATOR = '_';
	private static final String UNNAME_PREFIX = "UNNAMED"; //$NON-NLS-1$

	/**
	 * Get a uniqueName for columnName
	 * 
	 * @param orgColumnNameSet
	 *            the old column name set
	 * @param newColumnNameSet
	 *            the column name set
	 * @param columnNativeName
	 *            the column native name
	 * @param index
	 *            the index
	 * @return the unique column name
	 */

	public static String getUniqueColumnName( Set orgColumnNameSet,
			Set newColumnNameSet, String columnNativeName, int index )
	{
		String newColumnName;
		if ( columnNativeName == null
				|| columnNativeName.trim( ).length( ) == 0
				|| newColumnNameSet.contains( columnNativeName ) )
		{
			// name conflict or no name,give this column a unique name

			if ( columnNativeName == null
					|| columnNativeName.trim( ).length( ) == 0 )
				newColumnName = UNNAME_PREFIX + RENAME_SEPARATOR
						+ String.valueOf( index + 1 );
			else
				newColumnName = columnNativeName + RENAME_SEPARATOR
						+ String.valueOf( index + 1 );

			int i = 1;
			while ( orgColumnNameSet.contains( newColumnName )
					|| newColumnNameSet.contains( newColumnName ) )
			{
				newColumnName += String.valueOf( RENAME_SEPARATOR ) + i;
				i++;
			}
		}
		else
		{
			newColumnName = columnNativeName;
		}
		return newColumnName;
	}

	/**
	 * Get a unique name for dataset parameter
	 * 
	 * @param parameters
	 * @return
	 */
	
	public static final String getParamUniqueName( Iterator parametersIter,
			List retList, int position )
	{
		int n = 1;
		String prefix = "param"; //$NON-NLS-1$
		StringBuffer buf = new StringBuffer( );
		while ( buf.length( ) == 0 )
		{
			buf.append( prefix ).append( n++ );
			if ( parametersIter != null )
			{
				Iterator iter = parametersIter;
				if ( iter != null )
				{
					while ( iter.hasNext( ) && buf.length( ) > 0 )
					{
						DataSetParameterHandle parameter = (DataSetParameterHandle) iter
								.next( );
						if ( buf.toString( ).equalsIgnoreCase(
								parameter.getName( ) ) )
						{
							if ( parameter.getPosition( ) != null
									&& parameter.getPosition( ).intValue( ) == position )
								break;
							buf.setLength( 0 );
						}
					}
				}
				iter = retList.iterator( );
				while ( iter.hasNext( ) && buf.length( ) > 0 )
				{
					DataSetParameter parameter = (DataSetParameter) iter.next( );
					if ( buf.toString( )
							.equalsIgnoreCase( parameter.getName( ) ) )
					{
						if ( parameter.getPosition( ) != null
								&& parameter.getPosition( ).intValue( ) == position )
							break;
						buf.setLength( 0 );
					}
				}
			}
		}
		return buf.toString( );
	}
}
