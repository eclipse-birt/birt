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

import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * Represents an extended element. An extended item represents a custom element
 * added by the application. Extended items can use user-defined properties, can
 * use scripts, or a combination of the two. Extended items often require
 * user-defined properties.
 * <p>
 * An extended element has a plug-in property that is a name of a Java class
 * that implements the behavior for the element.
 * 
 * @see org.eclipse.birt.report.model.elements.ExtendedItem
 */

public class ExtendedItemHandle extends ReportItemHandle
{

	/**
	 * Constructs the handle with the report design and the element it holds.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public ExtendedItemHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 */

	public String getExtensionName( )
	{
		return getStringProperty( ExtendedItem.EXTENSION_NAME_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getDefn()
	 */

	public IElementDefn getDefn( )
	{
		ElementDefn extDefn = ( (ExtendedItem) getElement( ) ).getExtDefn( );
		if ( extDefn != null )
			return extDefn;

		return super.getDefn( );
	}

	/**
	 * Loads the instance of extended element. When the application invokes UI
	 * for the extended element, such as listing property values in property
	 * sheet, set the value of the extension-defined properties and so other
	 * operations, the application must create an instance of the extension
	 * element first. The created extended element reads its information cached
	 * by the handle and de-serialize the extension model.
	 * 
	 * @throws ExtendedElementException
	 *             if the serialized model is invalid
	 */

	public void loadExtendedElement( ) throws ExtendedElementException
	{
		( (ExtendedItem) getElement( ) ).initializeReportItem( design );
	}

	/**
	 * Returns the interface <code>IReportItem</code> for extension.
	 * 
	 * @return the interface <code>IReportItem</code> for extension
	 * 
	 * @throws ExtendedElementException
	 *             if the serialized model is invalid
	 */

	public IReportItem getReportItem( ) throws ExtendedElementException
	{
		IReportItem reportItem = ( (ExtendedItem) getElement( ) )
				.getExtendedElement( );

		if ( reportItem == null )
		{
			loadExtendedElement( );
			reportItem = ( (ExtendedItem) getElement( ) ).getExtendedElement( );
		}

		return reportItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.IExtendableElementHandle#getExtensionPropertyDefinitionList()
	 */
	
	public List getExtensionPropertyDefinitionList( )
	{
		return ( (ExtendedItem) getElement( ) ).getExtDefn( )
				.getLocalProperties( );
	}

}