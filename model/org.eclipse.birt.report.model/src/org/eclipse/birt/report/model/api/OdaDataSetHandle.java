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
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * Represents an extended data set.
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSet
 */

public class OdaDataSetHandle extends DataSetHandle
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
	 * Returns the cached response.
	 * 
	 * @return the cached response
	 * 
	 * @deprecated by the method {@link #getPrivateDriverDesignState()}
	 */

	public String getCachedResponse( )
	{
		return getStringProperty( OdaDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP );
	}

	/**
	 * Returns the private driver design state.
	 * 
	 * @return the private driver design state
	 */

	public String getPrivateDriverDesignState( )
	{
		return getStringProperty( OdaDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP );
	}

	/**
	 * Returns the script for query.
	 * 
	 * @return the script for query .
	 */

	public String getQueryScript( )
	{
		return getStringProperty( OdaDataSet.QUERY_SCRIPT_METHOD );
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
	 * @return the data set type
	 */

	public String getType( )
	{
		return getStringProperty( OdaDataSet.TYPE_PROP );
	}

	/**
	 * Returns the query type.
	 * 
	 * @return the query type
	 * 
	 * @deprecated by the {@link #getType()}
	 */

	public String getQueryType( )
	{
		return getStringProperty( OdaDataSet.TYPE_PROP );
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
	 */

	public void setPrivateDriverDesignState( String state )
			throws SemanticException
	{
		setStringProperty( OdaDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP, state );
	}

	/**
	 * Sets the cached response.
	 * 
	 * @param response
	 *            the response to set
	 * @throws SemanticException
	 *             if this property is locked.
	 * 
	 * @deprecated by the {@link #setPrivateDriverDesignState(String)}
	 */

	public void setCachedResponse( String response ) throws SemanticException

	{
		setStringProperty( OdaDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP,
				response );
	}

	/**
	 * Sets the query script.
	 * 
	 * @param script
	 *            the script to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setQueryScript( String script ) throws SemanticException
	{
		setStringProperty( OdaDataSet.QUERY_SCRIPT_METHOD, script );
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
	 * @param type
	 *            the type to set
	 * @throws SemanticException
	 *             if this property is locked.
	 */

	public void setType( String type ) throws SemanticException
	{
		setStringProperty( OdaDataSet.TYPE_PROP, type );
	}

	/**
	 * Sets the query type.
	 * 
	 * @param type
	 *            the type to set
	 * @throws SemanticException
	 *             if this property is locked.
	 * 
	 * @deprecated by the {@link #setType(String)}
	 */

	public void setQueryType( String type ) throws SemanticException
	{
		setStringProperty( OdaDataSet.TYPE_PROP, type );
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
	 */

	public String getExtensionName( )
	{
		return getStringProperty( OdaDataSet.EXTENSION_NAME_PROP );
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
	 * Returns a public driver property value with the given property name.
	 * 
	 * @param name
	 *            the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	public String getPublicDriverProperty( String name )
	{
		return ExtendedPropertyHelper.getExtendedProperty( this,
				OdaDataSet.PUBLIC_DRIVER_PROPERTIES_PROP, name );
	}

	/**
	 * Sets a public driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value of the property will be overwritten.
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

	public void setPublicDriverProperty( String name, String value )
			throws SemanticException
	{
		ExtendedPropertyHelper.setExtendedProperty( this,
				OdaDataSet.PUBLIC_DRIVER_PROPERTIES_PROP, name, value );
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