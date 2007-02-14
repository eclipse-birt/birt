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
import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;

/**
 * Represents a Hierarchy.
 * 
 * @see org.eclipse.birt.report.model.elements.olap.Hierarchy
 */

public class HierarchyHandle extends ReportElementHandle
		implements
			IHierarchyModel
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

	public HierarchyHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the data set of this hierarchy.
	 * 
	 * @return the handle to the data set
	 */

	public DataSetHandle getDataSet( )
	{
		return (DataSetHandle) getElementProperty( DATA_SET_PROP );
	}

	/**
	 * Sets the data set of this hierarchy.
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
	 * Returns the list of primary keys. The element in the list is a
	 * <code>String</code>.
	 * 
	 * @return a list of primary keys if set, otherwise null
	 */

	public List getPrimaryKeys( )
	{
		return getListProperty( PRIMARY_KEYS_PROP );
	}

	/**
	 * Gets the count of the level elements within this hierarchy.
	 * 
	 * @return count of the level elements if set, otherwise 0
	 */
	public int getLevelCount( )
	{
		return getPropertyHandle( LEVELS_PROP ).getContentCount( );
	}

	/**
	 * Gets the level handle by the name within this hierarchy.
	 * 
	 * @param levelName
	 *            name of the level to find
	 * @return the level within this hierarchy if found, otherwise null
	 */
	public LevelHandle getLevel( String levelName )
	{
		DesignElementHandle levelHandle = this.getModuleHandle( ).findCube(
				levelName );
		if ( levelHandle instanceof LevelHandle
				&& levelHandle.getContainer( ) == this )
			return (LevelHandle) levelHandle;
		return null;
	}

	/**
	 * Gets the level handle at the specified position within this hierarchy.
	 * 
	 * @param index
	 *            0-based integer
	 * @return the level handle at the given index, <code>null</code> if index
	 *         is out of range
	 */
	public LevelHandle getLevel( int index )
	{
		return (LevelHandle) getPropertyHandle( LEVELS_PROP )
				.getContent( index );
	}

	/**
	 * Returns an iterator for the filter list defined on this hierarchy. Each
	 * object returned is of type <code>StructureHandle</code>.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on this hierarchy.
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Indicates whether this hierarchy is default in the dimension.
	 * 
	 * @return true if this dimension is default in the cube, otherwise false
	 */
	public boolean isDefault( )
	{
		return getBooleanProperty( IS_DEFAULT_PROP );
	}

	/**
	 * Sets the status to indicate whether this hierarchy is default in the
	 * dimension.
	 * 
	 * @param isDefault
	 *            status whether this hierarchy is default in the dimension
	 * @throws SemanticException
	 */
	public void setDefault( boolean isDefault ) throws SemanticException
	{
		setProperty( IS_DEFAULT_PROP, Boolean.valueOf( isDefault ) );
	}
	
	/**
	 * Returns an iterator for the access controls. Each object returned is of
	 * type <code>AccessControlHandle</code>.
	 * 
	 * @return the iterator for user accesses defined on this cube.
	 */

	public Iterator accessControlsIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( ACCESS_CONTROLS_PROP );
		return propHandle.getContents( ).iterator( );
	}
}
