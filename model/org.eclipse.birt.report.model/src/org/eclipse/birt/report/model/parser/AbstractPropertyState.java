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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Parses the abstract property. The XML file is like:
 * 
 * <pre>
 * 
 *                                   
 *     &lt;property-tag name=&quot;propName&quot;&gt;property value&lt;/property-tag&gt;
 *   
 *  
 * </pre>
 * 
 * The supported tags are:
 * <ul>
 * <li>property,
 * <li>expression,
 * <li>xml,
 * <li>method,
 * <li>structure,
 * <li>list-property,
 * <li>text-property,
 * <li>html-property
 * </ul>
 * This class parses the "name" attribute and keeps it. Other attributes are
 * parsed by the inherited classes.
 */

public class AbstractPropertyState extends AbstractParseState
{

	/**
	 * The design file parser handler.
	 */

	protected DesignParserHandler handler = null;

	/**
	 * The element holding this property.
	 */

	protected DesignElement element = null;

	/**
	 * The element property name or structure member name.
	 */

	protected String name = null;

	/**
	 * The structure which holds this property as a member.
	 */

	protected IStructure struct = null;

	/**
	 * Whether the property of this state is defined.
	 */

	protected boolean valid = true;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * This constructor is used when this property to parse is a property of one
	 * element.
	 * 
	 * @param theHandler
	 *            the design file parser handler
	 * @param element
	 *            the element which holds this property
	 */

	public AbstractPropertyState( DesignParserHandler theHandler,
			DesignElement element )
	{
		handler = theHandler;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{

		name = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
		if ( StringUtil.isBlank( name ) )
		{
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_NAME_REQUIRED );
			handler.semanticError( e );
			valid = false;
			return;
		}
		
		super.parseAttrs( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler( )
	{
		return handler;
	}

	/**
	 * Sets the member of a structure.
	 * 
	 * @param struct
	 *            the structure that contains the member to set
	 * @param propName
	 *            the property in which the structure appears
	 * @param member
	 *            the structure member name
	 * @param value
	 *            the value parsed from the XML file
	 */

	void setMember( IStructure struct, String propName, String member,
			String value )
	{
		// Ensure that the member is defined.

		StructureDefn structDefn = (StructureDefn) struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn memberDefn = (StructPropertyDefn) structDefn
				.getMember( member );
		if ( memberDefn == null )
		{
			DesignParserException e = new DesignParserException( null,
					new String[]{member},
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
			RecoverableError.dealUndefinedProperty( handler, e );

			valid = false;
			return;
		}

		String valueToSet = value;
		if ( memberDefn.getTypeCode( ) != PropertyType.LITERAL_STRING_TYPE )
			valueToSet = StringUtil.trimString( valueToSet );

		if ( StringUtil.isBlank( valueToSet ) )
			return;

		if ( memberDefn.isEncryptable( ) )
		{
			IEncryptionHelper helper = MetaDataDictionary.getInstance( )
					.getEncryptionHelper( );
			valueToSet = helper.decrypt( valueToSet );
		}

		doSetMember( propName, memberDefn, valueToSet );
	}

	protected void doSetMember( String propName, StructPropertyDefn memberDefn,
			String valueToSet )
	{
		// Validate the value.

		try
		{
			Object propValue = memberDefn.validateXml( handler.getDesign( ),
					valueToSet );
			struct.setProperty( memberDefn, propValue );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propName + "." + memberDefn.getName( ) ); //$NON-NLS-1$
			handleMemberValueException( ex, memberDefn );
			valid = false;
		}
	}

	/**
	 * Sets the value of a property with a string parsed from the XML file.
	 * Performs any required semantic checks.
	 * 
	 * @param propName
	 *            property name
	 * @param value
	 *            value string from the XML file
	 */

	protected void setProperty( String propName, String value )
	{
		assert propName != null;

		if ( propName.equalsIgnoreCase( DesignElement.NAME_PROP )
				|| propName.equalsIgnoreCase( DesignElement.EXTENDS_PROP ) )
		{
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_INVALID_PROPERTY_SYNTAX );
			handler.semanticError( e );
			valid = false;
			return;
		}

		// The property definition is not found, including user
		// properties.

		ElementPropertyDefn propDefn = element.getPropertyDefn( propName );
		if ( propDefn == null )
		{
			DesignParserException e = new DesignParserException( null,
					new String[]{propName},
					DesignParserException.DESIGN_EXCEPTION_UNDEFINED_PROPERTY );
			RecoverableError.dealUndefinedProperty( handler, e );
			valid = false;
			return;
		}

		String valueToSet = value;
		if ( propDefn.getTypeCode( ) != PropertyType.LITERAL_STRING_TYPE )
			valueToSet = StringUtil.trimString( valueToSet );

		if ( StringUtil.isBlank( valueToSet ) )
			return;

		doSetProperty( propDefn, valueToSet );
	}

	protected void doSetProperty( PropertyDefn propDefn, String valueToSet )
	{
		// Validate the value.

		try
		{
			Object propValue = propDefn.validateXml( handler.getDesign( ),
					valueToSet );
			element.setProperty( propDefn, propValue );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propDefn.getName( ) );
			handlePropertyValueException( ex );
			valid = false;
		}
	}

	/**
	 * Process the property value exception if the value of a property is
	 * invalid.
	 * 
	 * @param e
	 *            the property value exception
	 */

	private void handlePropertyValueException( PropertyValueException e )
	{
		String propName = e.getPropertyName( );

		if ( isRecoverableError( e.getErrorCode( ), e.getElement( )
				.getPropertyDefn( propName ) ) )
			RecoverableError.dealInvalidPropertyValue( handler, e );
		else
			handler.semanticError( e );
	}

	/**
	 * Process the property value exception if the value of a member is invalid.
	 * 
	 * @param e
	 *            the property value exception
	 * @param memberDefn
	 *            the member definition
	 */

	private void handleMemberValueException( PropertyValueException e,
			StructPropertyDefn memberDefn )
	{
		if ( isRecoverableError( e.getErrorCode( ), memberDefn ) )
			RecoverableError.dealInvalidMemberValue( handler, e, struct,
					memberDefn );
		else
			handler.semanticError( e );
	}

	/**
	 * Checks whether the given exception is an error that the parser can
	 * recover.
	 * 
	 * @param errorCode
	 *            the error code of the property value exception
	 * @param propDefn
	 *            the definition of the exception. Can be an element property
	 *            definition or a member definition.
	 * @return return <code>true</code> if it is a recoverable error,
	 *         otherwise <code>false</code>.
	 */

	private boolean isRecoverableError( String errorCode, IPropertyDefn propDefn )
	{

		if ( PropertyValueException.DESIGN_EXCEPTION_NEGATIVE_VALUE
				.equalsIgnoreCase( errorCode )
				|| PropertyValueException.DESIGN_EXCEPTION_NON_POSITIVE_VALUE
						.equalsIgnoreCase( errorCode )
				|| PropertyValueException.DESIGN_EXCEPTION_UNIT_NOT_ALLOWED
						.equalsIgnoreCase( errorCode )
				|| PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED
						.equalsIgnoreCase( errorCode )
				|| PropertyValueException.DESIGN_EXCEPTION_UNIT_REQUIRED
						.equalsIgnoreCase( errorCode ) )
			return true;

		if ( PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE
				.equalsIgnoreCase( errorCode )
				|| PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND
						.equalsIgnoreCase( errorCode ) )
		{
			if ( FormatValue.CATEGORY_MEMBER.equalsIgnoreCase( propDefn
					.getName( ) ) )
				return true;
		}

		return false;
	}

	/**
	 * Sets the value of the attribute "name". This method is used when the
	 * specific state is defined. When the generic state jumps to the specific
	 * one, the <code>parseAttrs</code> will not be called. So the value of
	 * the attribute "name" should be set by the generic state.
	 * 
	 * @param name
	 *            the name to set
	 */

	protected void setName( String name )
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#jumpTo()
	 */

	public AbstractParseState jumpTo( )
	{
		// If this state can not be parsed properly, any states in it are
		// ignored.

		if ( !valid )
			return new AnyElementState( getHandler( ) );

		return super.jumpTo( );
	}
}