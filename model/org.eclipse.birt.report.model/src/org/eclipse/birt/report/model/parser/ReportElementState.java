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

import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.ExtendsException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.util.StringUtil;
import org.xml.sax.Attributes;

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
		ElementDefn containerDefn = (ElementDefn) container.getDefn( );
		ElementDefn contentDefn = (ElementDefn) content.getDefn( );

		// The following conditions should not occur in the parser --
		// they indicate parser design errors since they can be prevented
		// with the right syntax checks.

		assert containerDefn.isContainer( );
		SlotDefn slotInfo = (SlotDefn) containerDefn.getSlot( slotID );
		assert slotInfo != null;
		assert slotInfo.canContain( content );

		// If this is a single-item slot, ensure that the slot is empty.

		if ( !slotInfo.isMultipleCardinality( )
				&& container.getSlot( slotID ).getCount( ) > 0 )
		{
			handler.semanticError( new ContentException( container, slotID,
					ContentException.DESIGN_EXCEPTION_SLOT_IS_FULL ) );
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
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
				return false;
			}

			if ( name != null )
			{
				if ( ns.getElement( name ) != null )
				{
					handler.semanticError( new NameException( container, name,
							NameException.DESIGN_EXCEPTION_DUPLICATE ) );
					return false;
				}
				DesignElement parent = content.getExtendsElement( );
				if ( id == RootElement.ELEMENT_NAME_SPACE && parent != null )
				{
					if ( !design.getSlot( ReportDesign.COMPONENT_SLOT )
							.contains( parent ) )
					{
						handler
								.semanticError( new ExtendsException(
										content,
										content.getElementName( ),
										ExtendsException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT ) );
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
						NameException.DESIGN_EXCEPTION_NAME_REQUIRED ) );
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
				handler
						.semanticError( new DesignParserException(
								DesignParserException.DESIGN_EXCEPTION_ILLEGAL_EXTENDS ) );
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
		ElementDefn defn = (ElementDefn) element.getDefn( );

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

}