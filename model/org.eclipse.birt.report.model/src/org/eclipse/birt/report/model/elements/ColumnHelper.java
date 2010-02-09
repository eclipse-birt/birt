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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;

/**
 * Provides methods for style property values on the column. Currently, only
 * TableItem and GridItem have columns.
 * 
 */

public final class ColumnHelper
{

	/**
	 * Figures out the column according to the index of the column.
	 * 
	 * @param module
	 *            the report design
	 * @param columnSlot
	 *            the slot contains columns
	 * @param columnIndex
	 *            the 1-based column index
	 * 
	 * @return the column at the specified position in the slot, or null if not
	 *         found.
	 */

	public static TableColumn findColumn( Module module,
			ContainerSlot columnSlot, int columnIndex )
	{
		assert columnIndex > 0;

		for ( int i = 0, index = 0; i < columnSlot.getCount( ); i++ )
		{
			TableColumn column = (TableColumn) ( columnSlot.getContent( i ) );

			index += getColumnRepeat( module, column );

			if ( index >= columnIndex )
				return column;
		}

		return null;
	}
	
	/**
	 * Gets the repeat times of the column.
	 * 
	 * @param module
	 *            the module.
	 * @param column
	 *            the column.
	 * @return the column length.
	 */
	private static int getColumnRepeat( Module module, TableColumn column )
	{
		int repeat = column.getIntProperty( module,
				ITableColumnModel.REPEAT_PROP );

		// in default, repeat is one.

		repeat = ( repeat <= 0 ) ? 1 : repeat;
		return repeat;
	}
}