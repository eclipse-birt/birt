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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.namespace.IModuleNameSpace;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

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
	 * Logger instance.
	 */

	private static Logger logger = Logger
			.getLogger( ElementRefPropertyType.class.getName( ) );
	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.element"; //$NON-NLS-1$

	/**
	 * Constructor.
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
	 * @throws PropertyValueException
	 *             if the target element is of different meta definition as the
	 *             one defined in the <code>defn</code>.
	 */

	public Object validateValue( Module module, PropertyDefn defn, Object value )
			throws PropertyValueException
	{
		if ( value == null )
			return null;

		ElementDefn targetDefn = (ElementDefn) defn.getTargetElementType( );
		if ( value instanceof String )
		{
			logger.log( Level.FINE,
					"Validate the value of the element reference property as a string: " //$NON-NLS-1$
							+ value );
			return validateStringValue( module, targetDefn, defn,
					(String) value );
		}
		if ( value instanceof DesignElement )
		{
			logger.log( Level.FINE,
					"Validate the value of the element reference property as a DesignElement " //$NON-NLS-1$
							+ ( (DesignElement) value ).getName( ) );
			DesignElement target = (DesignElement) value;
			return validateElementValue( module, targetDefn, defn, target );
		}

		// Invalid property value.

		logger.log( Level.SEVERE, "Invalid value type: " + value ); //$NON-NLS-1$
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				PropertyType.ELEMENT_REF_TYPE );
	}

	/**
	 * Validates the element name.
	 * 
	 * @param module
	 *            report design
	 * @param targetDefn
	 *            definition of target element
	 * @param name
	 *            element name
	 * @return the resolved element reference value
	 * @throws PropertyValueException
	 *             if the type of target element is not that target definition,
	 *             or the element with the given name is not in name space.
	 */

	private ElementRefValue validateStringValue( Module module,
			ElementDefn targetDefn, PropertyDefn propDefn, String name )
			throws PropertyValueException
	{
		name = StringUtil.trimString( name );
		if ( name == null )
			return null;

		IModuleNameSpace elementResolver = module
				.getModuleNameSpace( targetDefn.getNameSpaceID( ) );

		ElementRefValue refValue = null;

		// special case for theme property since it can be direcly referred.

		refValue = elementResolver.resolve( name, propDefn );

		assert refValue != null;

		// Element is unresolved.

		if ( !refValue.isResolved( ) )
			return refValue;

		DesignElement target = refValue.getElement( );
		assert target != null;

		// Check type.

		if ( !target.getDefn( ).isKindOf( targetDefn ) )
			throw new PropertyValueException( target.getName( ),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE,
					PropertyType.ELEMENT_REF_TYPE );

		// Resolved reference.

		return refValue; // new ElementRefValue( target );
	}

	/**
	 * Validates the element value.
	 * 
	 * @param module
	 *            report design
	 * @param targetDefn
	 *            definition of target element
	 * @param target
	 *            target element
	 * @return the resolved element reference value
	 * @throws PropertyValueException
	 *             if the type of target element is not that target definition.
	 */

	private ElementRefValue validateElementValue( Module module,
			ElementDefn targetDefn, PropertyDefn propDefn, DesignElement target )
			throws PropertyValueException
	{
		IModuleNameSpace elementResolver = module
				.getModuleNameSpace( targetDefn.getNameSpaceID( ) );
		ElementRefValue refValue = elementResolver.resolve( target, propDefn );

		// Check type.

		if ( !target.getDefn( ).isKindOf( targetDefn ) )
			throw new PropertyValueException( target.getName( ),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE,
					PropertyType.ELEMENT_REF_TYPE );

		// Resolved reference.

		return refValue;
	}

	/**
	 * Converts this property type into a string, return the element name of the
	 * referenced element.
	 * 
	 * @return the element name of the referenced element, return
	 *         <code>null</code> if value is null;
	 */

	public String toString( Module module, PropertyDefn defn, Object value )
	{
		if ( value == null )
			return null;

		if ( value instanceof String )
			return (String) value;

		ElementRefValue refValue = (ElementRefValue) value;

		if ( !StyledElement.STYLE_PROP.equals( defn.getName( ) ) )
		{
			return ReferenceValueUtil.needTheNamespacePrefix(
					(ReferenceValue) value, module );
		}

		return refValue.getName( );
	}

	/**
	 * Resolves an element reference. Look up the name in the name space of the
	 * target element type. If the target is found, replace the element name
	 * with the cached element.
	 * 
	 * @param module
	 *            the report design
	 * @param defn
	 *            the definition of the element ref property
	 * @param ref
	 *            the element reference
	 */

	public void resolve( Module module, PropertyDefn defn, ElementRefValue ref )
	{
		if ( ref.isResolved( ) )
			return;
		ElementDefn targetDefn = (ElementDefn) defn.getTargetElementType( );

		// Let the corresponding name scope do the resolve things for the
		// element reference value. The scope will search the target element not
		// only in the current root namespace, but also in the included
		// libraries namespace.

		IModuleNameSpace elementResolver = module
				.getModuleNameSpace( targetDefn.getNameSpaceID( ) );

		String name = ReferenceValueUtil.needTheNamespacePrefix( ref, module );

		// special case for theme property since it can be direcly referred.

		DesignElement target = null;
		target = elementResolver.resolve( name, defn ).getElement( );

		if ( target != null )
			ref.resolve( target );
	}
}