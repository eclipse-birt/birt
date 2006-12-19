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

package org.eclipse.birt.report.model.api.olap;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;

/**
 * Represents a level element.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Level
 */

public class LevelHandle extends ReportElementHandle implements ILevelModel
{

	/**
	 * Constructs a handle for the given design and design element. The
	 * application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public LevelHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the column name of this level.
	 * 
	 * @return column name of this level
	 */
	public String getColumnName( )
	{
		return getStringProperty( COLUMN_NAME_PROP );
	}

	/**
	 * Sets the column name for this level.
	 * 
	 * @param columnName
	 *            the column name to set
	 * @throws SemanticException
	 *             property is locked
	 */
	public void setColumnName( String columnName ) throws SemanticException
	{
		setStringProperty( COLUMN_NAME_PROP, columnName );
	}

	/**
	 * Returns the iterator of attributes. The element in the iterator is a
	 * <code>String</code>.
	 * 
	 * @return the iterator of attribute string list
	 */

	public Iterator attributesIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ATTRIBUTES_PROP );

		assert propHandle != null;

		return propHandle.iterator( );
	}
}
