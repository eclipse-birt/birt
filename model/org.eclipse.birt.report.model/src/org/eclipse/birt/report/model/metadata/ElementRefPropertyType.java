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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Represents a reference to an element. An element reference is different from
 * a slot. A slot <em>contains</em> an element. An element reference simply
 * <em>references</em> an element defined elsewhere.
 * <p>
 * An element reference can be in one of two states: resolved or unresolved. A
 * resolved reference points to an the "target" element itself. An unresolved
 * reference gives only the name of the target element, and the element itself
 * may or may not exist. This allows the model to handle designs that refer to
 * template elements that have since been removed or renamed.
 * <p>
 * Elements that contain properties of this type must provide code to perform
 * semantic checks on the reference property. This is done to avoid the need to
 * search the property list to find any properties that are of this type.
 * <p>
 * The reference value are stored as an <code>ElementRefValue</code>
 * 
 * @see ElementRefValue
 */

public class ElementRefPropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.element"; //$NON-NLS-1$

	/**
	 * Constructor
	 */

	public ElementRefPropertyType( )
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
		return ELEMENT_REF_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	public String getName( )
	{
		return ELEMENT_REF_NAME;
	}

	/**
	 * Validates an element reference value and returns a corresponding
	 * <code>ElementRefValue</code> that reference the target element. The
	 * target element to be referenced can be identified by its name or the
	 * element instance.
	 * 
	 * @return the corresponding <code>ElementRefValue</code>, it will be
	 *         resolved if the target element is found in the namespace. Return
	 *         <code>null</code> if value is null.
	 * 
	 * @throws PropertyValueException
	 *             if the target element is of different meta definition as the
	 *             one defined in the <code>defn</code>.
	 *  
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;

		ElementDefn targetDefn = defn.getTargetElementType( );
		if ( value instanceof String )
		{
			String name = StringUtil.trimString( (String) value );
			NameSpace ns = design.getNameSpace( targetDefn.getNameSpaceID( ) );
			DesignElement target = ns.getElement( name );

			// Element is unresolved.

			if ( target == null )
				return new ElementRefValue( name );

			// Check type.

			if ( !targetDefn.isKindOf( target.getDefn( ) ) )
				throw new PropertyValueException( target.getName( ),
						PropertyValueException.WRONG_ELEMENT_TYPE,
						PropertyType.ELEMENT_REF_TYPE );

			// Resolved reference.

			return new ElementRefValue( target );
		}
		if ( value instanceof DesignElement )
		{
			// Check type.

			DesignElement target = (DesignElement) value;
			if ( !targetDefn.isKindOf( target.getDefn( ) ) )
				throw new PropertyValueException( target.getName( ),
						PropertyValueException.WRONG_ELEMENT_TYPE,
						PropertyType.ELEMENT_REF_TYPE );

			// Resolved reference.

			return new ElementRefValue( target );
		}

		// Invalid property value.

		throw new PropertyValueException( value,
				PropertyValueException.INVALID_VALUE,
				PropertyType.ELEMENT_REF_TYPE );
	}

	/**
	 * Converts this property type into a string, return the element name of the
	 * referenced element.
	 * 
	 * @return the element name of the referenced element, return
	 *         <code>null</code> if value is null;
	 *  
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		return ( (ElementRefValue) value ).getName( );
	}

	/**
	 * Resolves an element reference. Look up the name in the name space of the
	 * target element type. If the target is found, replace the element name
	 * with the cached element.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            the definition of the element ref property
	 * @param ref
	 *            the element reference
	 */

	public void resolve( ReportDesign design, PropertyDefn defn,
			ElementRefValue ref )
	{
		if ( ref.isResolved( ) )
			return;
		ElementDefn targetDefn = defn.getTargetElementType( );
		NameSpace ns = design.getNameSpace( targetDefn.getNameSpaceID( ) );
		DesignElement target = ns.getElement( ref.getName( ) );
		if ( target != null )
			ref.resolve( target );
	}

}