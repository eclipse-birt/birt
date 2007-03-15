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

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;

/**
 * Represents a cube.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Cube
 */

public class TabularCubeHandle extends CubeHandle implements ITabularCubeModel
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

	public TabularCubeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data set of this cube.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		return (DataSetHandle) getElementProperty( DATA_SET_PROP );
	}

	/**
	 * Sets the data set of this cube.
	 * 
	 * @param handle
	 *            the handle of the data set
	 * 
	 * @throws SemanticException
	 *             if the property is locked, or the data-set is invalid.
	 */

	public void setDataSet( DataSetHandle handle ) throws SemanticException
	{
		if ( handle == null )
			setStringProperty( DATA_SET_PROP, null );
		else
		{
			ModuleHandle moduleHandle = handle.getRoot( );
			String valueToSet = handle.getName( );
			if ( moduleHandle instanceof LibraryHandle )
			{
				String namespace = ( (LibraryHandle) moduleHandle )
						.getNamespace( );
				valueToSet = StringUtil.buildQualifiedReference( namespace,
						handle.getName( ) );
			}
			setStringProperty( DATA_SET_PROP, valueToSet );
		}
	}

	/**
	 * Adds a dimension condition to this cube.
	 * 
	 * @param condition
	 * @return the added dimension condition handle if succeed
	 * @throws SemanticException
	 */
	
	public DimensionConditionHandle addDimensionCondition(
			DimensionCondition condition ) throws SemanticException
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		return (DimensionConditionHandle) propertyHandle.addItem( condition );
	}

	/**
	 * Adds a dimension condition to the specified position.
	 * 
	 * @param condition
	 * @param posn
	 * @return the added dimension condition handle if succeed
	 * @throws SemanticException
	 */
	public DimensionConditionHandle addDimensionCondition(
			DimensionCondition condition, int posn ) throws SemanticException
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		return (DimensionConditionHandle) propertyHandle.insertItem( condition,
				posn );
	}

	/**
	 * Gets the iterator of the join conditions. Each one in the iterator is
	 * instance of <code>StructureHandle</code>.
	 * 
	 * @return iterator of the join conditions in this cube
	 */
	
	public Iterator joinConditionsIterator( )
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		return propertyHandle.iterator( );
	}

	/**
	 * Removes a dimension condition from this cube.
	 * 
	 * @param condition
	 * @throws SemanticException
	 */
	public void removeDimensionCondition( DimensionCondition condition )
			throws SemanticException
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		propertyHandle.removeItem( condition );
	}

	/**
	 * 
	 * @param conditionHandle
	 * @throws SemanticException
	 */
	public void removeDimensionCondition(
			DimensionConditionHandle conditionHandle ) throws SemanticException
	{
		PropertyHandle propertyHandle = getPropertyHandle( DIMENSION_CONDITIONS_PROP );
		IStructure struct = conditionHandle == null ? null : conditionHandle
				.getStructure( );
		propertyHandle.removeItem( struct );
	}
}
