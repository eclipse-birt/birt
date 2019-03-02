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

package org.eclipse.birt.report.model.util.impl;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;

public class ActionHelper
{

	/**
	 * The element handle that defines the action property.
	 */
	protected final DesignElementHandle elementHandle;

	/**
	 * Internal name of the action property.
	 */
	protected final String actionPropName;

	/**
	 * 
	 * @param elementHandle
	 * @param propName
	 */
	public ActionHelper( DesignElementHandle elementHandle, String propName )
	{
		this.elementHandle = elementHandle;
		this.actionPropName = propName;
	}

	/**
	 * Returns a handle to work with the action property, action is a structure
	 * that defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle( )
	{
		PropertyHandle propHandle = elementHandle
				.getPropertyHandle( actionPropName );
		List actions = (List) propHandle.getValue( );

		if ( actions == null || actions.isEmpty( ) )
			return null;
		Action action = (Action) actions.get( 0 );
		return (ActionHandle) action.getHandle( propHandle );
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action
	 *            new action to be set on the image, it represents a bookmark
	 *            link, hyper-link, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * 
	 * @throws SemanticException
	 *             if member of the action is not valid.
	 */

	public ActionHandle setAction( Action action ) throws SemanticException
	{
		elementHandle.setProperty( actionPropName, null );
		PropertyHandle propHandle = elementHandle
				.getPropertyHandle( actionPropName );
		propHandle.addItem( action );

		List listValue = (List) elementHandle.getElement( ).getProperty(
				elementHandle.getModule( ), actionPropName );
		Action actionValue = (Action) listValue.get( 0 );
		if ( actionValue == null )
			return null;
		return (ActionHandle) actionValue.getHandle( propHandle );
	}

	/**
	 * Returns the iterator for action defined on this element. T
	 * 
	 * @return the iterator for <code>Action</code> structure list defined on
	 *         this element
	 */

	public Iterator<ActionHandle> actionsIterator( )
	{
		PropertyHandle propHandle = elementHandle
				.getPropertyHandle( actionPropName );
		assert propHandle != null;
		return propHandle.iterator( );
	}
}
