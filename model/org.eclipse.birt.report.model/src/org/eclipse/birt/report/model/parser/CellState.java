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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a cell within a table item.
 * 
 */

public class CellState extends ReportElementState
{

	/**
	 * The cell being created.
	 */

	protected Cell element = null;

	/**
	 * Constructs the cell state with the design parser handler, the container
	 * element and the container slot of the cell.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public CellState( ModuleParserHandler handler, DesignElement theContainer,
			int slot )
	{
		super( handler, theContainer, slot );
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new Cell( );

		initSimpleElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
			return new TextItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.AUTO_TEXT_TAG ) )
			return new AutoTextState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
			return new LabelState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
			return new DataItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_TAG ) )
			return new ListItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
			return new TableItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
			return new FreeFormState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
			return new GridItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
			return new ImageState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LINE_TAG ) )
			return new LineItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.BROWSER_CONTROL_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
			return new ExtendedItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
			return new TextDataItemState( handler, element, ICellModel.CONTENT_SLOT );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.TEMPLATE_REPORT_ITEM_TAG ) )
			return new TemplateReportItemState( handler, element,
					ICellModel.CONTENT_SLOT );
		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		makeTestExpressionCompatible( );
	}

}
