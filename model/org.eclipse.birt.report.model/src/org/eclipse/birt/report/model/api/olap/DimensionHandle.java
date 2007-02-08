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

import java.util.List;

import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;

/**
 * Represents a dimension element in the cube element.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Dimension
 */

public class DimensionHandle extends ReportElementHandle
		implements
			IDimensionModel
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

	public DimensionHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Indicates whether this dimension is a special type of Time.
	 * 
	 * @return true if this dimension is of Time type, otherwise false
	 */

	public boolean isTimeType( )
	{
		return getBooleanProperty( IS_TIME_TYPE_PROP );
	}

	/**
	 * Sets the status to indicate whether this dimension is a special type of
	 * Time.
	 * 
	 * @param isTimeType
	 *            status whether this dimension is of Time type
	 * @throws SemanticException
	 *             the property is locked
	 */

	public void setTimeType( boolean isTimeType ) throws SemanticException
	{
		setProperty( IS_TIME_TYPE_PROP, Boolean.valueOf( isTimeType ) );
	}

	/**
	 * Indicates whether this dimension is default in the cube.
	 * 
	 * @return true if this dimension is default in the cube, otherwise false
	 */
	public boolean isDefault( )
	{
		return getBooleanProperty( IS_DEFAULT_PROP );
	}

	/**
	 * Sets the status to indicate whether this dimension is default in the
	 * cube.
	 * 
	 * @param isDefault
	 *            status whether this dimension is default in the cube
	 * @throws SemanticException
	 */
	public void setDefault( boolean isDefault ) throws SemanticException
	{
		setProperty( IS_DEFAULT_PROP, Boolean.valueOf( isDefault ) );
	}

	/**
	 * Gets the default hierarchy for the dimension. Return the first hierarchy
	 * that is set to default if found, otherwise return the first hierarchy in
	 * the list if exists, otherwise null.
	 * 
	 * @return the default hierarchy for this dimension
	 */
	public HierarchyHandle getDefaultHierarchy( )
	{
		List contents = getContents( HIERARCHIES_PROP );
		if ( contents.isEmpty( ) )
			return null;
		for ( int i = 0; i < contents.size( ); i++ )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) contents.get( i );
			if ( hierarchy.isDefault( ) )
				return hierarchy;
		}
		return (HierarchyHandle) contents.get( 0 );
	}
}
