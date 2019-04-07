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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;

/**
 * CrosstabMemberValueHandle
 */
public class MemberValueHandle extends ContentElementHandle
		implements
			IMemberValueModel
{

	/**
	 * Constructs a member value handle with the given design and the element.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public MemberValueHandle( Module module, DesignElement element )
	{
		super( module, element );

	}

	/**
	 * Gets the value of this member value handle.
	 * 
	 * @return value of this member
	 */
	public String getValue( )
	{
		return getStringProperty( VALUE_PROP );
	}

	/**
	 * Sets the value of this member value.
	 * 
	 * @param value
	 *            the value to set
	 * @throws SemanticException
	 */
	public void setValue( String value ) throws SemanticException
	{
		setStringProperty( VALUE_PROP, value );
	}

	/**
	 * Gets name of the referred cube level element.
	 * 
	 * @return name of the referred cube level
	 */
	public String getCubeLevelName( )
	{
		return getStringProperty( LEVEL_PROP );
	}

	/**
	 * Gets the cube level handle for this member value.
	 * 
	 * @return the referred cube level handle if resolved, otherwise null
	 */
	public LevelHandle getLevel( )
	{
		return (LevelHandle) getElementProperty( LEVEL_PROP );
	}

	/**
	 * Sets the referred level of this member value.
	 * 
	 * @param levelHandle
	 * @throws SemanticException
	 */
	public void setLevel( LevelHandle levelHandle ) throws SemanticException
	{
		if ( levelHandle == null )
			setStringProperty( LEVEL_PROP, null );
		else
		{
			/*
			 * ModuleHandle moduleHandle = levelHandle.getRoot( ); String
			 * valueToSet = levelHandle.getElement( ).getFullName( ); if (
			 * moduleHandle instanceof LibraryHandle ) { String namespace = (
			 * (LibraryHandle) moduleHandle ) .getNamespace( ); valueToSet =
			 * StringUtil.buildQualifiedReference( namespace, valueToSet ); }
			 * setStringProperty( LEVEL_PROP, valueToSet );
			 */
			setProperty( LEVEL_PROP, levelHandle );
		}
	}

	/**
	 * Returns the iterator for filter list defined on this member value.
	 * 
	 * @return the iterator for <code>FilterCond</code> structure list defined
	 *         on a table or list.
	 */

	public Iterator<FilterConditionHandle> filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}
}
