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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
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
		implements
			IExtendedItemModel
{

	/**
	 * Constructs the handle with the report design and the element it holds.
	 * The application generally does not create handles directly. Instead, it
	 * uses one of the navigation methods available on other element handles.
	 * 
	 * @param module
	 *            the module
	 * @param element
	 *            the model representation of the element
	 */

	public ExtendedItemHandle( Module module, DesignElement element )
	{
		super( module, element );
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 */

	public String getExtensionName( )
	{
		return getStringProperty( EXTENSION_NAME_PROP );
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
		( (ExtendedItem) getElement( ) ).initializeReportItem( module );
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
		if ( ( (ExtendedItem) getElement( ) ).getExtDefn( ) != null )

			return ( (ExtendedItem) getElement( ) ).getExtDefn( )
					.getLocalProperties( );

		return Collections.EMPTY_LIST;

	}

	/**
	 * Returns the methods defined on the extension element definition and the
	 * methods defined within the extension model property inside.
	 * 
	 * @return the list of methods
	 */

	public List getMethods( )
	{
		return ( (ExtendedItem) getElement( ) ).getMethods( );
	}

	/**
	 * Returns an iterator over filter. The iterator returns instances of
	 * <code>FilterConditionHandle</code> that represents filter condition
	 * object.
	 * 
	 * @return iterator over filters.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.FilterCondition
	 */

	public Iterator filtersIterator( )
	{
		PropertyHandle propHandle = getPropertyHandle( FILTER_PROP );
		assert propHandle != null;
		return propHandle.iterator( );
	}

	/**
	 * Returns the external script defined in the extended element model.
	 * 
	 * @return the script
	 */

	public String getExternalScript( )
	{
		String propName = ( (ExtendedItem) getElement( ) )
				.getScriptPropertyName( );
		if ( propName == null )
			return null;
		return getStringProperty( propName );
	}

	/**
	 * Sets the scripts in the extension element model.
	 * 
	 * @param theScript
	 *            the script to be set
	 * @throws SemanticException
	 *             if fail to set the scripts
	 */

	public void setExternalScript( String theScript ) throws SemanticException
	{
		String propName = ( (ExtendedItem) getElement( ) )
				.getScriptPropertyName( );
		if ( propName == null )
			return;
		setStringProperty( propName, theScript );
	}

	/**
	 * Returns the alternate text of this extended item.
	 * 
	 * @return the alternate text of the extended item.
	 */

	public String getAltText( )
	{
		return getStringProperty( ALT_TEXT_PROP );
	}

	/**
	 * Returns the resource key of the alternate text of this extended item.
	 * 
	 * @return the resource key of the alternate text
	 */

	public String getAltTextKey( )
	{
		return getStringProperty( ALT_TEXT_KEY_PROP );
	}

	/**
	 * Sets the alt text of this extended item.
	 * 
	 * @param altText
	 *            the alt text
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setAltText( String altText ) throws SemanticException
	{
		setStringProperty( ALT_TEXT_PROP, altText );
	}

	/**
	 * Sets the alt text id of this extended item.
	 * 
	 * @param altTextKey
	 *            the alt text id
	 * 
	 * @throws SemanticException
	 *             if the property is locked.
	 */

	public void setAltTextKey( String altTextKey ) throws SemanticException
	{
		setStringProperty( ALT_TEXT_KEY_PROP, altTextKey );
	}

}