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

import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IGraphicMaterPageModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * This class parses a graphic master page.
 * 
 */

public class GraphicMasterPageState extends MasterPageState
{

	/**
	 * Constructs the graphic master page state with the design file parser
	 * handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public GraphicMasterPageState( ModuleParserHandler handler )
	{
		super( handler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new GraphicMasterPage( );
		initElement( attrs, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CONTENTS_TAG ) )
			return new ContentsState( );
		return super.startElement( tagName );
	}

	/**
	 * Parses the list of "page decoration" items on the master page itself.
	 */

	class ContentsState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
				return new LabelState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
				return new DataItemState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LINE_TAG ) )
				return new LineItemState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.RECTANGLE_TAG ) )
				return new RectangleState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
				return new GridItemState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.BROWSER_CONTROL_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
				return new FreeFormState( handler, element,
						IGraphicMaterPageModel.CONTENT_SLOT );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new AnyElementState( handler );
			return super.startElement( tagName );
		}
	}

}
