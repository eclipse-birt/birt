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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.namespace.IModuleNameSpace;

/**
 * Property type for the "extends" property of an element. The value is either
 * the unresolved name of the parent element, or a cached pointer to the parent
 * element. The parent element must always be of the same element type as the
 * derived element.
 * 
 */

public class ExtendsPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.extends"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ExtendsPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return EXTENDS_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return EXTENDS_TYPE_NAME;
	}

	/**
	 * Validates an extends property value of an element. The value can be a
	 * string value that takes the name of the target element, or it can be the
	 * instance of the target element.
	 * 
	 * @return An <code>ElementRefValue</code> that holds the target element,
	 *         the reference is resolved if the input value is the instance of
	 *         the target element.
	 */

	public Object validateValue( Module module, PropertyDefn defn, Object value )
			throws PropertyValueException
	{
		if ( value == null )
			return null;

		// This implementation assumes that the class-specific validation
		// was already done.

		if ( value instanceof String )
		{
			String name = StringUtil.trimString( (String) value );
			if ( name == null )
				return null;

			String namespace = StringUtil.extractNamespace( name );
			name = StringUtil.extractName( name );
			
			// Element is unresolved.

			return new ElementRefValue( namespace, name );
		}
		if ( value instanceof DesignElement )
		{
			DesignElement target = (DesignElement) value;
			IModuleNameSpace elementResolver = module
					.getModuleNameSpace( ( (ElementDefn) target.getDefn( ) )
							.getNameSpaceID( ) );
			ElementRefValue refValue = elementResolver.resolve( target );

			// Resolved reference.

			return refValue; // new ElementRefValue( null, (DesignElement) value );
		}

		// Invalid property value.

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				PropertyType.ELEMENT_REF_TYPE );
	}

	/**
	 * Returns the referenced element name if the input value is an
	 * <code>ElementRefValue</code>, return <code>null</code> if the value
	 * is null.
	 */

	public String toString( Module module, PropertyDefn defn, Object value )
	{
		ElementRefValue refValue = (ElementRefValue) value;
		if ( refValue == null )
			return null;

		return refValue.getQualifiedReference();
	}

}
