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
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * This class parses a master page.
 * 
 */

public abstract class MasterPageState extends ReportElementState
{

	/**
	 * The master page being created.
	 */

	protected MasterPage	element	= null;

	/**
	 * Constructs the master page state with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public MasterPageState( DesignParserHandler handler )
	{
		super( handler, handler.getDesign( ), ReportDesign.PAGE_SLOT );
	}

	/**
	 * Child master page state must overwrite this method to create an instance
	 * of the specified master page, then initialize it, and finally call the
	 * super method. for example, in <code>GraphicMasterPage</code>, the method will look
	 * like:
	 * <pre>
	 * public void parseAttrs( Attributes attrs ) throws XMLParserException {
	 * 		element = new GraphicMasterPage();
	 * 		initElements( attrs );
	 * 		super.parseAttrs( attrs );
	 * 		...
	 * }
	 * </pre>
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		setProperty( MasterPage.TYPE_PROP, attrs
				.getValue( DesignSchemaConstants.TYPE_ATTRIB ) );
		setProperty( MasterPage.WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.WIDTH_ATTRIB ) );
		setProperty( MasterPage.HEIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.HEIGHT_ATTRIB ) );
		setProperty( MasterPage.ORIENTATION_PROP, attrs
				.getValue( DesignSchemaConstants.ORIENTATION_ATTRIB ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STYLE_TAG ) )
			return new StyleState( handler, element );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.MARGINS_TAG ) )
			return new MarginsState( );
		return super.startElement( tagName );
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

	/**
	 * Parses the element that holds the page margins.
	 */

	class MarginsState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */
		
		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			setProperty( MasterPage.TOP_MARGIN_PROP, attrs
					.getValue( DesignSchemaConstants.TOP_ATTRIB ) );
			setProperty( MasterPage.LEFT_MARGIN_PROP, attrs
					.getValue( DesignSchemaConstants.LEFT_ATTRIB ) );
			setProperty( MasterPage.BOTTOM_MARGIN_PROP, attrs
					.getValue( DesignSchemaConstants.BOTTOM_ATTRIB ) );
			setProperty( MasterPage.RIGHT_MARGIN_PROP, attrs
					.getValue( DesignSchemaConstants.RIGHT_ATTRIB ) );
		}
	}

}
