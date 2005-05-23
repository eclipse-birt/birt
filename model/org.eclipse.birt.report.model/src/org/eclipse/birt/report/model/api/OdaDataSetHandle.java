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
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * Represents an extended data set.
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSet
 */

public class OdaDataSetHandle extends DataSetHandle implements IOdaDataSetModel
{

	/**
	 * Constructs a handle for extended data set report item. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the model representation of the element
	 */

	public OdaDataSetHandle( ReportDesign design, DesignElement element )
	{
		super( design, element );
	}

	/**
	 * Returns the private driver design state.
	 * 
	 * @return the private driver design state
	 * @deprecated
	 */

	public String getPrivateDriverDesignState( )
	{
		return null;
	}

	/**
	 * Returns the script for query.
     * 
     * @deprecated to be removed.
	 * @return the script for query .
	 */

	public String getQueryScript( )
	{
		return null;
	}

	/**
	 * Returns the query text.
	 * 
	 * @return the query text.
	 */

	public String getQueryText( )
	{
		return getStringProperty( OdaDataSet.QUERY_TEXT_PROP );
	}

	/**
	 * Returns the data set type.
	 * 
	 * @deprecated type has been replaced by extension ID
	 * @return the data set type
	 */

	public String getType( )
	{
		return null;
	}

	/**
	 * Returns the result set name.
	 * 
	 * @return the result set name
	 */

	public String getResultSetName( )
	{
		return getStringProperty( OdaDataSet.RESULT_SET_NAME_PROP );
	}

	/**
	 * Sets the private driver design state.
	 * 
	 * @param state
	 *            the design state to set
	 * @throws SemanticException
	 *             if this property is locked.
	 * @deprecated
	 */

	public void setPrivateDriverDesignState( String state )
			throws SemanticException
	{
	}

	/**
	 * Sets the query script.
	 * 
     * @deprecated to be removed.
     * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if this property is locked.
	 * @deprecated
	 */

	public void setQueryScript( String script ) throws SemanticException
	{
	}

	/**
	 * Sets the query text.
	 * 
	 * @param text
	 *            the text to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setQueryText( String text ) throws SemanticException
	{
		setStringProperty( OdaDataSet.QUERY_TEXT_PROP, text );
	}

	/**
	 * Sets the type.
	 * 
	 * @deprecated type has been replaced by extension ID
	 * @param type
	 *            the type to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setType( String type ) throws SemanticException
	{
	}

	/**
	 * Sets the result set name.
	 * 
	 * @param name
	 *            the name to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setResultSetName( String name ) throws SemanticException
	{
		setStringProperty( OdaDataSet.RESULT_SET_NAME_PROP, name );
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 * @deprecated use <code>getExtensionID()</code>
	 */

	public String getExtensionName( )
	{
		return null;
	}

	/**
	 * Returns ID of the extension which extends this ODA data set.
	 * 
	 * @return the extension ID
	 */

	public String getExtensionID( )
	{
		return getStringProperty( OdaDataSet.EXTENSION_ID_PROP );
	}

	/**
	 * Returns the iterator for the private driver property list. The item over
	 * the iterator is the instance of <code>ExtendedPropertyHandle</code>.
	 * 
	 * @return the iterator over private driver property list defined on this
	 *         data set.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty
	 */

	public Iterator privateDriverPropertiesIterator( )
	{
		PropertyHandle propertyHandle = getPropertyHandle( OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP );
		assert propertyHandle != null;

		return propertyHandle.iterator( );
	}

	/**
	 * Returns a private driver property value with the given property name.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	public String getPrivateDriverProperty( String name )
	{
		return ExtendedPropertyHelper.getExtendedProperty( this,
				OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP, name );
	}

	/**
	 * Sets a private driver property value with the given name and value. If
	 * the property does not exist, it will be added into the property list. If
	 * the property already exists, the value of the property will be
	 * overwritten.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * @param value
	 *            the value of a public driver property
	 * 
	 * @throws SemanticException
	 *             if <code>name</code> is <code>null</code> or an empty
	 *             string after trimming.
	 */

	public void setPrivateDriverProperty( String name, String value )
			throws SemanticException
	{
		ExtendedPropertyHelper.setExtendedProperty( this,
				OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP, name, value );
	}

	/**
	 * Returns the element definition of the element this handle represents.
	 * 
	 * @return the element definition of the element this handle represents.
	 */

	public IElementDefn getDefn( )
	{
		ElementDefn extDefn = ( (OdaDataSet) getElement( ) ).getExtDefn( );
		if ( extDefn != null )
			return extDefn;

		return super.getDefn( );
	}

	/**
	 * Returns the list of extension property definition. All these properties
	 * are just those defined in extension plugin.
	 * 
	 * @return the list of extension property definition.
	 */

	public List getExtensionPropertyDefinitionList( )
	{
		if ( ( (OdaDataSet) getElement( ) ).getExtDefn( ) != null )

			return ( (OdaDataSet) getElement( ) ).getExtDefn( )
					.getLocalProperties( );

		return Collections.EMPTY_LIST;
	}
}