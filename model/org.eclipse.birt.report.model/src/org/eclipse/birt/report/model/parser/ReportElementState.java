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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.ExtendsException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.command.UserPropertyException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.structures.PropertyMask;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base class for all report element parse states.
 *  
 */

public abstract class ReportElementState extends DesignParseState
{

	/**
	 * The element that contains the one being parsed.
	 */

	protected DesignElement container = null;

	/**
	 * The slot within the container in which the element being parsed is to
	 * appear.
	 */

	protected int slotID = 0;

	/**
	 * Values for the local property values. The contents are of type Object.
	 */

	protected HashMap propValues;

	/**
	 * Temporary storage of the list of property masks.
	 */

	protected ArrayList propMasks;

	/**
	 * Constructs the report element state with the design parser handler, the
	 * container element and the container slot of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ReportElementState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler );
		container = theContainer;
		slotID = slot;
	}

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            SAX handler for the design file parser
	 */
	public ReportElementState( DesignParserHandler theHandler )
	{
		super( theHandler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public abstract DesignElement getElement( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COMMENTS_TAG ) )
			return new TextState( handler, getElement( ),
					DesignElement.COMMENTS_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DISPLAY_NAME_TAG ) )
			return new ExternalTextState( handler, getElement( ),
					DesignElement.DISPLAY_NAME_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_DEFN_TAG ) )
			return new UserPropertyDefnState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_MASK_TAG ) )
			return new PropertyMaskState( );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_VALUE_TAG ) )
			return new PropertyValueState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CUSTOM_TAG ) )
			return new TextState( handler, getElement( ),
					DesignElement.CUSTOM_XML_PROP );
		return super.startElement( tagName );
	}

	/**
	 * The property values may be before the definition, so there will be the
	 * check if the value is valid. If valid, we set property value, otherwise
	 * throws semantic error.
	 * <p>
	 * The property masks may also be before the definition, so there will be
	 * the check if the mask is valid. If valid, we keep property mask,
	 * otherwise throws semantic error.
	 * 
	 * @throws SAXException
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{

		DesignElement element = getElement( );

		if ( propValues != null )
		{
			Iterator iter = propValues.keySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Object name = iter.next( );
				if ( element.getPropertyDefn( name.toString( ) ) == null )
				{
					handler
							.semanticError( new UserPropertyException( element,
									name.toString( ),
									UserPropertyException.NOT_FOUND ) );
				}
				else
				{
					setProperty( name.toString( ), propValues.get( name )
							.toString( ) );
				}
			}
		}
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the
	 * ReportElement tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	/**
	 * Parses the "PropertyValue" tag which provides the value of a user-defined
	 * property. The name of the property value is required and the property
	 * definition of the same name must exist in the element or its parent. The
	 * value of the property must be valid for the property type.
	 */

	class PropertyValueState extends InnerParseState
	{

		protected String name;
		protected String value;

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
				handler.semanticError( new UserPropertyException(
						getElement( ), name,
						UserPropertyException.NAME_REQUIRED ) );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			value = text.toString( );
			if ( ! StringUtil.isBlank( name ) && ! StringUtil.isBlank( value ) )
			{
				// The design file may provide two or more values for the same
				// property. The general rule is that the last value "wins"; any
				// previous values are ignored without warning.

				if ( propValues == null )
					propValues = new HashMap( );
				propValues.put( name, value );
			}
		}
	}

	/**
	 * Adds an element to the given slot. Records a semantic error and returns
	 * false if an error occurs. (Does not throw an exception because we don't
	 * want to terminate the parser: we want to keep parsing to find other
	 * errors.)
	 * 
	 * @param container
	 *            the container element
	 * @param slotID
	 *            the slot within the container
	 * @param content
	 *            the content element
	 * @return true if the add was successful, false if an error occurred and a
	 *         semantic error was logged
	 */

	protected boolean addToSlot( DesignElement container, int slotID,
			DesignElement content )
	{
		ElementDefn containerDefn = container.getDefn( );
		ElementDefn contentDefn = content.getDefn( );

		// The following conditions should not occur in the parser --
		// they indicate parser design errors since they can be prevented
		// with the right syntax checks.

		assert containerDefn.isContainer( );
		SlotDefn slotInfo = containerDefn.getSlot( slotID );
		assert slotInfo != null;
		assert slotInfo.canContain( content );

		// If this is a single-item slot, ensure that the slot is empty.

		if ( !slotInfo.isMultipleCardinality( )
				&& container.getSlot( slotID ).getCount( ) > 0 )
		{
			handler.semanticError( new ContentException( container, slotID,
					ContentException.SLOT_IS_FULL ) );
			return false;
		}

		// The name should not be null if it is required. The parser state
		// should have already caught this case.

		String name = content.getName( );
		assert !StringUtil.isBlank( name )
				|| contentDefn.getNameOption( ) != MetaDataConstants.REQUIRED_NAME;

		// Disallow duplicate names.

		ReportDesign design = handler.getDesign( );
		int id = contentDefn.getNameSpaceID( );
		if ( id != MetaDataConstants.NO_NAME_SPACE )
		{
			NameSpace ns = design.getNameSpace( id );
			if ( name == null
					&& contentDefn.getNameOption( ) == MetaDataConstants.REQUIRED_NAME )
			{
				handler.semanticError( new NameException( container, name,
						NameException.NAME_REQUIRED ) );
				return false;
			}

			if ( name != null )
			{
				if ( ns.getElement( name ) != null )
				{
					handler.semanticError( new NameException( container, name,
							NameException.DUPLICATE ) );
					return false;
				}
				DesignElement parent = content.getExtendsElement( );
				if ( id == RootElement.ELEMENT_NAME_SPACE && parent != null )
				{
					if ( !design.getSlot( ReportDesign.COMPONENT_SLOT )
							.contains( parent ) )
					{
						handler.semanticError( new ExtendsException( content,
								content.getElementName( ),
								ExtendsException.PARENT_NOT_IN_COMPONENT ) );
						return false;
					}
				}
				ns.insert( content );
			}
		}

		// Add the item to the element ID map if we are using
		// element IDs.

		if ( MetaDataDictionary.getInstance( ).useID( ) )
		{
			content.setID( design.getNextID( ) );
			design.addElementID( content );
		}

		// Add the item to the container.

		container.getSlot( slotID ).add( content );

		// Cache the inverse relationship.

		content.setContainer( container, slotID );

		return true;
	}

	/**
	 * Initializes a report element.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 * @param nameRequired
	 *            true if this element requires a name, false if the name is
	 *            optional.
	 */

	protected void initElement( Attributes attrs, boolean nameRequired )
	{
		DesignElement element = getElement( );
		String name = attrs.getValue( DesignElement.NAME_PROP );
		if ( StringUtil.isBlank( name ) )
		{
			if ( nameRequired )
				handler.semanticError( new NameException( element, null,
						NameException.NAME_REQUIRED ) );
		}
		else
			element.setName( name );
		if ( element.getDefn( ).canExtend( ) )
		{
			element.setExtendsName( attrs
					.getValue( DesignSchemaConstants.EXTENDS_ATTRIB ) );
			resolveExtendsElement( );
		}
		else
		{
			// If "extends" is set on an element that can not be extended,
			// exception will be thrown.

			if ( !StringUtil.isBlank( attrs
					.getValue( DesignSchemaConstants.EXTENDS_ATTRIB ) ) )
				handler.semanticError( new DesignParserException(
						DesignParserException.ILLEGAL_EXTENDS ) );
		}
		if ( !addToSlot( container, slotID, element ) )
			return;

	}

	/**
	 * Resolves the reference of the extend. There is an assumption that the
	 * parent element always exists before his derived ones.
	 *  
	 */

	private void resolveExtendsElement( )
	{
		DesignElement element = getElement( );
		ReportDesign design = handler.getDesign( );
		ElementDefn defn = element.getDefn( );

		// Resolve extends

		int id = defn.getNameSpaceID( );
		assert id != MetaDataConstants.NO_NAME_SPACE;
		NameSpace ns = design.getNameSpace( id );
		String extendsName = element.getExtendsName( );
		if ( StringUtil.isBlank( extendsName ) )
			return;
		DesignElement parent = ns.getElement( extendsName );
		try
		{
			element.checkExtends( parent );
			element.setExtendsElement( parent );
		}
		catch ( ExtendsException ex )
		{
			handler.semanticError( ex );
		}
	}

	/**
	 * Parses the "PropertyMask" tag which provides the mask of a BIRT or user
	 * defined property. The name of the property mask is required and the
	 * property definition of the same name must exist in the element or its
	 * parent. The value of the mask must be valid for PropertyMask choices in
	 * {@link org.eclipse.birt.report.model.elements.DesignChoiceConstants}.
	 */

	class PropertyMaskState extends InnerParseState
	{

		/**
		 * The temporary storage of one property mask.
		 */

		private PropertyMask mask;

		/**
		 * Creates a new PropertyMaskState according to the design file.
		 */

		public PropertyMaskState( )
		{
			propMasks = (ArrayList) getElement( ).getLocalProperty(
					handler.design, DesignElement.PROPERTY_MASKS_PROP );

			if ( propMasks == null )
				propMasks = new ArrayList( );

			mask = new PropertyMask( );

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
			setMember( mask, DesignElement.PROPERTY_MASKS_PROP,
					PropertyMask.NAME_MEMBER, name );

			String value = attrs.getValue( DesignSchemaConstants.MASK_ATTRIB );
			if ( !StringUtil.isBlank( value ) )
			{
				setMember( mask, DesignElement.PROPERTY_MASKS_PROP,
						PropertyMask.MASK_MEMBER, value );
			}
			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			if ( propMasks != null )
			{
				propMasks.add( mask );
			}
			getElement( ).setProperty( DesignElement.PROPERTY_MASKS_PROP,
					propMasks );

			propMasks = null;
			super.end( );
		}
	}

}