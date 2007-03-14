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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Extended Item (extended item) tag.
 */

public class ExtendedItemState extends ReportItemState
{

	/**
	 * The extended item being created.
	 */

	public ExtendedItem element;

	/**
	 * Constructs the extended item state with the design parser handler, the
	 * container element and the container slot of the extended item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ExtendedItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Constructs extended item state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param prop
	 *            the slot in which this element appears
	 */

	public ExtendedItemState( ModuleParserHandler handler,
			DesignElement theContainer, String prop )
	{
		super( handler, theContainer, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new ExtendedItem( );

		parseExtensionName( attrs, true );
		if ( element.getExtDefn( ) == null )
			element.initializeContentTree( );

		boolean nameRequired = element.getDefn( ).getNameOption( ) == MetaDataConstants.REQUIRED_NAME;
		initElement( attrs, nameRequired );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( element.getExtDefn( ) != null )
		{
			return super.startElement( tagName );
		}
		return ParseStateFactory.createParseState( tagName, handler, element,
				element.getContentTree( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		if ( handler.versionNumber >= VersionUtil.VERSION_3_2_1 )
		{
			super.end( );
			return;
		}

		try
		{
			element.initializeReportItem( handler.module );
		}
		catch ( ExtendedElementException e )
		{
			return;
		}

		Object reportItem = element.getExtendedElement( );

		if ( reportItem != null && reportItem instanceof ICompatibleReportItem )
		{
			List jsExprs = ( (ICompatibleReportItem) reportItem )
					.getRowExpressions( );
			Map updatedExprs = BoundDataColumnUtil.handleJavaExpression(
					jsExprs, element, handler.module, handler.tempValue );
			( (ICompatibleReportItem) reportItem )
					.updateRowExpressions( updatedExprs );
		}

		super.end( );
	}
}