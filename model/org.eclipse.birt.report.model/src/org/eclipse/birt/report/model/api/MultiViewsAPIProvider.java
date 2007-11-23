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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;

/**
 * Implements functions to deal with API-level views operations. Through these
 * APIs, the caller does not need to anything about
 * <code>AbstractMultiViewHandle</code> or <code>MultiViewHandle</code>.
 */

public class MultiViewsAPIProvider implements IMultiViewsModel
{

	/**
	 * The element.
	 */

	private ReportItemHandle element;

	/**
	 * The name of the property of which instance is a subclass of
	 * <code>AbstractMultiViewHandle</code>.
	 */

	private String propertyName;

	/**
	 * The constructor.
	 * 
	 * @param element
	 *            the element
	 * @param propName
	 *            the property name. Corresponding property value must be is a
	 *            subclass of <code>AbstractMultiViewHandle</code>.
	 */

	public MultiViewsAPIProvider( ReportItemHandle element, String propName )
	{
		this.element = element;
		propertyName = propName;

		if ( this.element == null )
			throw new IllegalArgumentException(
					"Must provide a NON-NULL element." ); //$NON-NLS-1$

		if ( propName == null )
			throw new IllegalArgumentException(
					"Must provide the name for the views property." ); //$NON-NLS-1$

		IPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn.getTypeCode( ) != IPropertyType.ELEMENT_TYPE )
			throw new IllegalArgumentException(
					"The views property must defined as element type." ); //$NON-NLS-1$

	}

	/**
	 * Returns the view that is being used.
	 * 
	 * @return the view that is being used
	 */

	public DesignElementHandle getCurrentView( )
	{
		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element
				.getProperty( propertyName );
		if ( multiView == null ||
				multiView.getCurrentViewIndex( ) == MultiViewsHandle.HOST )
			return element;

		MultiViewsElementProvider subProvider = new MultiViewsElementProvider(
				multiView );
		return subProvider.getCurrentView( );
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
		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element
				.getProperty( propertyName );

		ModuleHandle module = element.getModuleHandle( );
		CommandStack stack = module.getCommandStack( );
		stack.startTrans( null );
		try
		{
			if ( multiView == null )
			{
				multiView = module.getElementFactory( ).newMultiView( );
				element.setProperty( propertyName, multiView );
			}

			MultiViewsElementProvider subProvider = new MultiViewsElementProvider(
					multiView );
			subProvider.addView( viewElement );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );
	}

	/**
	 * Deletes the given view.
	 * 
	 * @param viewElement
	 *            the element
	 * @throws SemanticException
	 */

	public void dropView( DesignElementHandle viewElement )
			throws SemanticException
	{
		AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element
				.getProperty( propertyName );
		if ( multiView == null )
			return;

		MultiViewsElementProvider subProvider = new MultiViewsElementProvider(
				multiView );
		subProvider.dropView( viewElement );
	}

	/**
	 * Sets the index for the view to be used. If the given element is not in
	 * the multiple view, it will be added and set as the active view.
	 * 
	 * @param viewElement
	 *            the view element
	 * 
	 * @throws SemanticException
	 *             if the given element resides in the other elements.
	 */

	public void setCurrentView( DesignElementHandle viewElement )
			throws SemanticException
	{
		if ( viewElement == null )
			return;

		// if the viewElement is in the design tree and not in table, throw
		// exception

		DesignElement internalElement = element.getElement( );
		if ( viewElement.getContainer( ) != null &&
				!viewElement.getElement( ).isContentOf( internalElement ) )
		{
			throw new PropertyValueException( internalElement, element
					.getPropertyDefn( propertyName ), null,
					PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );
		}

		ModuleHandle module = element.getModuleHandle( );
		CommandStack stack = module.getCommandStack( );
		stack.startTrans( null );
		try
		{
			AbstractMultiViewsHandle multiView = (AbstractMultiViewsHandle) element
					.getProperty( propertyName );
			if ( multiView == null )
			{
				multiView = module.getElementFactory( ).newMultiView( );
				element.setProperty( propertyName, multiView );
			}

			// if the viewElement is in the table and not in multiple view,
			// throw exception

			if ( viewElement.getContainer( ) != null &&
					!viewElement.getElement( ).isContentOf(
							multiView.getElement( ) ) )
			{
				throw new PropertyValueException( internalElement, element
						.getPropertyDefn( propertyName ), null,
						PropertyValueException.DESIGN_EXCEPTION_ITEM_NOT_FOUND );
			}

			// add to the multiple view

			if ( viewElement.getContainer( ) == null )
			{
				MultiViewsElementProvider subProvider = new MultiViewsElementProvider(
						multiView );
				subProvider.addView( viewElement );
			}

			// set index

			int newIndex = MultiViewsHandle.HOST;
			if ( viewElement != element )
			{
				ContainerContext context = new ContainerContext( multiView
						.getElement( ), MultiViewsHandle.VIEWS_PROP );
				newIndex = context.indexOf( viewElement.getElement( ) );

				// the viewElement is either added to the view or already in the
				// view

				assert newIndex != -1;
			}

			multiView.setCurrentViewIndex( newIndex );

		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );
	}

}
