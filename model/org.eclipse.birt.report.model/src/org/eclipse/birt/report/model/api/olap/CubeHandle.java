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

import java.util.Collections;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Measure;

/**
 * Represents a cube.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Cube
 */

public abstract class CubeHandle extends ReportElementHandle
		implements
			ICubeModel
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

	public CubeHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Gets the dimension with the specified name within this cube.
	 * 
	 * @param dimensionName
	 *            name of the dimension to find
	 * @return dimension within the cube if found, otherwise <code>null</code>
	 */
	public DimensionHandle getDimension( String dimensionName )
	{
		DesignElement dimension = module.findDimension( dimensionName );
		if ( dimension instanceof Dimension
				&& dimension.isContentOf( getElement( ) ) )
			return (DimensionHandle) dimension.getHandle( module );
		return null;
	}

	/**
	 * Gets the measure with the specified name within this cube.
	 * 
	 * @param measureName
	 *            name of the measure to find
	 * @return measure within the cube if found, otherwise <code>null</code>
	 */
	public MeasureHandle getMeasure( String measureName )
	{
		DesignElement measure = module.findOLAPElement( measureName );
		if ( measure instanceof Measure && measure.isContentOf( getElement( ) ) )
			return (MeasureHandle) measure.getHandle( module );
		return null;
	}

	/**
	 * Returns an iterator for the filter list defined on this cube. Each object
	 * returned is of type <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on this cube.
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Gets the default measure group for the cube.
	 * 
	 * @return the default measure group
	 */
	public MeasureGroupHandle getDefaultMeasureGroup( )
	{
		DesignElement measureGroup = ( (Cube) getElement( ) )
				.getDefaultMeasureGroup( module );
		return measureGroup == null ? null : (MeasureGroupHandle) measureGroup
				.getHandle( module );
	}

	/**
	 * Sets the default measure group for this cube.
	 * 
	 * @param defaultMeasureGroup
	 *            the default measure group to set
	 * @throws SemanticException
	 */
	public void setDefaultMeasureGroup( MeasureGroupHandle defaultMeasureGroup )
			throws SemanticException
	{
		setProperty( DEFAULT_MEASURE_GROUP_PROP, defaultMeasureGroup );
	}

	/**
	 * Returns an iterator for the access controls. Each object returned is of
	 * type <code>AccessControlHandle</code>.
	 * 
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator accessControlsIterator( )
	{
		return Collections.emptyList( ).iterator( );
	}

	/**
	 * Adds the filter condition.
	 * 
	 * @param fc
	 *            the filter condition structure
	 * @throws SemanticException
	 *             if the expression of filter condition is empty or null
	 */

	public void addFilter( FilterCondition fc ) throws SemanticException
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		propHandle.addItem( fc );
	}

	/**
	 * Removes the filter condition.
	 * 
	 * @param fc
	 *            the filter condition structure
	 * @throws SemanticException
	 *             if the given condition doesn't exist in the filters
	 */

	public void removeFilter( FilterCondition fc ) throws SemanticException
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		propHandle.removeItem( fc );
	}

	/**
	 * Gets the expression handle for the <code>ACLExpression</code> property.
	 * 
	 * @return
	 */
	public ExpressionHandle getACLExpression( )
	{
		return getExpressionProperty( ACL_EXPRESSION_PROP );
	}
}
