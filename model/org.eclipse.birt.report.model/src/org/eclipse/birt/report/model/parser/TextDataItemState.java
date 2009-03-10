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
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Multi Line Data (multi-line data item) tag.
 * 
 */

public class TextDataItemState extends ReportItemState
{

	/**
	 * The multi-line data item being created.
	 */

	public TextDataItem element;

	/**
	 * Constructs the multi-line data state with the design parser handler, the
	 * container element and the container slot of the multi-line data.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TextDataItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/**
	 * Constructs text data item state with the design parser handler, the
	 * container element and the container property name of the report element.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param prop
	 *            the slot in which this element appears
	 */

	public TextDataItemState( ModuleParserHandler handler,
			DesignElement theContainer, String prop )
	{
		super( handler, theContainer, prop );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TextDataItem( );
		initElement( attrs );
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
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */
	public void end( ) throws SAXException
	{
		super.end( );

		if ( handler.versionNumber < VersionUtil.VERSION_3_2_19 )
		{
			// for old design file, if hasExpression is not set and content is
			// set, the default value is TURE
			Object hasExpression = element.getLocalProperty( handler.module,
					ITextDataItemModel.HAS_EXPRESSION_PROP );
			Object content = element.getLocalProperty( handler.module,
					ITextDataItemModel.VALUE_EXPR_PROP );
			if ( hasExpression == null && content != null )
				element.setProperty( ITextDataItemModel.HAS_EXPRESSION_PROP,
						Boolean.TRUE );
		}
	}

}
