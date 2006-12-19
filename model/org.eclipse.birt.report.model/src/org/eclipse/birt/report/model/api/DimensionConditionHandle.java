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

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Represents the handle of the cube-dimension/hierarchy join condition.
 * 
 * <p>
 * <dl>
 * <dt><strong>Primary Keys </strong></dt>
 * <dd>Primary keys define a list of primary key to do the join actions between
 * cube and hierarchy in dimension. Each one in the list must be one of the data
 * set column in data set defined in cube.</dd>
 * <dt><strong>Hierarchy</strong></dt>
 * <dd>Hierarchy refers a hierarchy element in one of the dimension in the
 * cube.</dd>
 * </dl>
 * 
 */

public class DimensionConditionHandle extends StructureHandle
{

	/**
	 * Constructs the handle of the cube join condition.
	 * 
	 * @param valueHandle
	 *            the value handle for the cube join condition list of one
	 *            property
	 * @param index
	 *            the position of this join condition in the list
	 */

	public DimensionConditionHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Gets the list of the primary keys in this dimension condition. Each one
	 * in the list is instance of <code>String</code>.
	 * 
	 * @return a list of the primary keys in this dimension condition if
	 *         defined, null if not set
	 */
	public List getPrimaryKeys( )
	{
		return (List) getProperty( DimensionCondition.PRIMARY_KEYS_MEMBER );
	}

	/**
	 * Gets the referred hierarchy handle of this condition.
	 * 
	 * @return hierarchy handle of this condition if found, otherwise null
	 */
	public HierarchyHandle getHierarchy( )
	{
		ElementRefValue refValue = (ElementRefValue) ( (Structure) getStructure( ) )
				.getLocalProperty( getModule( ),
						DimensionCondition.HIERARCHY_MEMBER );
		if ( refValue == null || !refValue.isResolved( ) )
			return null;
		DesignElement element = refValue.getElement( );
		return (HierarchyHandle) element.getHandle( element.getRoot( ) );
	}

	/**
	 * Gets the referred hierarchy name of this condition.
	 * 
	 * @return hierarchy name of this condition if set, otherwise null
	 */
	public String getHierarchyName( )
	{
		return getStringProperty( DimensionCondition.HIERARCHY_MEMBER );
	}

	/**
	 * Sets the referred hierarchy by the name.
	 * 
	 * @param hierarchyName
	 *            the hierarchy name to set
	 * @throws SemanticException
	 */
	public void setHierarchy( String hierarchyName ) throws SemanticException
	{
		setProperty( DimensionCondition.HIERARCHY_MEMBER, hierarchyName );
	}

	/**
	 * Sets the referred hierarchy by the handle.
	 * 
	 * @param hierarchyHandle
	 *            the hierarchy handle to set
	 * @throws SemanticException
	 */
	public void setHierarchy( HierarchyHandle hierarchyHandle )
			throws SemanticException
	{
		DesignElement element = hierarchyHandle == null
				? null
				: hierarchyHandle.getElement( );
		setProperty( DimensionCondition.HIERARCHY_MEMBER, element );
	}

}
