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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;

/**
 * Represents a multiple view element. A view element can contains multiple
 * report items. The container of the view can use inner report items to
 * represents its appearance.
 */

public class MultiViewsHandle extends DesignElementHandle
		implements
			IMultiViewsModel
{

	/**
	 * Represents the container of the view does not use any inner view.
	 */

	public static final int HOST = -1;

	/**
	 * The target report element.
	 */

	protected MultiViews element;

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

	public MultiViewsHandle( Module module, MultiViews element )
	{
		super( module );
		this.element = element;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/**
	 * Returns a list containing views.
	 * 
	 * @return a list containing views. Each item is an
	 *         <code>ReportItemHandle</code>.
	 */

	protected List getViews( )
	{
		List list = getListProperty( VIEWS_PROP );
		if ( list == null )
			return Collections.EMPTY_LIST;

		List retList = new ArrayList( );
		retList.addAll( list );
		return Collections.unmodifiableList( retList );
	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView( )
	{
		int currentViewIndex = getCurrentViewIndex( );
		if ( currentViewIndex == HOST )
			return getContainer( );

		List views = getViews( );
		if ( views.isEmpty( ) || views.size( ) <= currentViewIndex )
			return null;

		return (DesignElementHandle) views.get( currentViewIndex );
	}

	/**
	 * Returns the index for the current view.
	 * 
	 * @return a 0-based integer
	 */

	protected int getCurrentViewIndex( )
	{
		return getIntProperty( INDEX_PROP );
	}

	/**
	 * Sets the index for the view to be used.
	 * 
	 * @param index
	 *            a 0-based integer
	 * 
	 * @throws SemanticException
	 */

	protected void setCurrentViewIndex( int index ) throws SemanticException
	{
		if ( index > HOST )
		{
			List views = getViews( );
			if ( views.isEmpty( ) || views.size( ) <= index )
				return;
		}
		else
			index = HOST;

		setProperty( INDEX_PROP, new Integer( index ) );
		return;
	}

	/**
	 * Adds a new element as the view.
	 * 
	 * @param viewElement
	 *            the element
	 * @throws SemanticException
	 */

	public void addView( DesignElementHandle viewElement )
			throws SemanticException
	{
		if ( viewElement == null )
			return;

		add( VIEWS_PROP, viewElement );
	}

	/**
	 * Deletes the given view. If the given element was named as the current
	 * view, this method also set the current view to <code>HOST</code>.
	 * 
	 * @param viewElement
	 *            the view element
	 * @throws SemanticException
	 */

	public void dropView( DesignElementHandle viewElement )
			throws SemanticException
	{
		if ( viewElement == null )
			return;

		CommandStack cmdStack = getModuleHandle( ).getCommandStack( );
		cmdStack.startTrans( null );
		try
		{
			DesignElementHandle currentView = getCurrentView( );
			if ( currentView == viewElement )
				setCurrentViewIndex( HOST );

			drop( VIEWS_PROP, viewElement );
		}
		catch ( SemanticException e )
		{
			cmdStack.rollback( );
			throw e;
		}

		cmdStack.commit( );
	}
}
