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
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.Attributes;

/**
 * Base class for all report item parse states.
 *  
 */

public abstract class ReportItemState extends ReportElementState
{

	/**
	 * Constructs the report item state with the design parser handler, the
	 * container element and the container slot of the report item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ReportItemState( DesignParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.VISIBILITY_TAG ) )
			return new VisibilityState( handler, getElement( ),
					ReportItem.VISIBILITY_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STYLE_TAG ) )
			return new StyleState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.BOOKMARK_TAG ) )
			return new TextState( handler, getElement( ),
					ReportItem.BOOKMARK_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
			return new TextState( handler, getElement( ), ReportItem.TOC_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG ) )
			return new MethodState( handler, getElement( ) );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.PARAM_BINDINGS_TAG ) )
			return new ParamBindingsState( handler, getElement( ),
					ReportItem.PARAM_BINDINGS_PROP );
		return super.startElement( tagName );
	}

	/**
	 * Intializes a report item with the properties common to all report items.
	 * 
	 * @param attrs
	 *            the SAX attributes object
	 */

	protected void initElement( Attributes attrs )
	{
		super.initElement( attrs, false );
		setProperty( ReportItem.X_PROP, attrs
				.getValue( DesignSchemaConstants.X_ATTRIB ) );
		setProperty( ReportItem.Y_PROP, attrs
				.getValue( DesignSchemaConstants.Y_ATTRIB ) );
		setProperty( ReportItem.HEIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.HEIGHT_ATTRIB ) );
		setProperty( ReportItem.WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.WIDTH_ATTRIB ) );
		setProperty( ReportItem.DATA_SET_PROP, attrs
				.getValue( DesignSchemaConstants.DATA_SET_ATTRIB ) );
	}

}