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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses an image item in the design file.
 * 
 */

public class ImageState extends ReportItemState
{

	/**
	 * The image item being created.
	 */

	protected ImageItem image;

	/**
	 * Constructs the image item state with the design parser handler, the
	 * container element and the container slot of the image item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ImageState( ModuleParserHandler handler, DesignElement theContainer,
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
		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		image = new ImageItem( );

		initElement( attrs );
	}

	/**
	 * Check whether the source type conflicts, and set the proper source type.
	 */

	private void checkImageType( )
	{

		int type = 0;
		Module module = handler.getModule( );

		String uri = (String) image.getLocalProperty( module,
				ImageItem.URI_PROP );
		if ( !StringUtil.isEmpty( uri ) )
		{
			uri = StringUtil.trimQuotes( uri );
			try
			{
				URL url = new URL( uri );
				setProperty( ImageItem.SOURCE_PROP,
						DesignChoiceConstants.IMAGE_REF_TYPE_URL );
			}
			catch ( MalformedURLException e )
			{
				setProperty( ImageItem.SOURCE_PROP,
						DesignChoiceConstants.IMAGE_REF_TYPE_FILE );
			}

			type++;
		}

		StructRefValue imageName = (StructRefValue) image.getLocalProperty(
				module, ImageItem.IMAGE_NAME_PROP );
		if ( imageName != null )
		{
			setProperty( ImageItem.SOURCE_PROP,
					DesignChoiceConstants.IMAGE_REF_TYPE_EMBED );
			type++;
		}

		String typeExpr = (String) image.getLocalProperty( module,
				ImageItem.TYPE_EXPR_PROP );
		String valueExpr = (String) image.getLocalProperty( module,
				ImageItem.VALUE_EXPR_PROP );

		if ( !StringUtil.isEmpty( typeExpr ) || !StringUtil.isEmpty( valueExpr ) )
		{
			setProperty( ImageItem.SOURCE_PROP,
					DesignChoiceConstants.IMAGE_REF_TYPE_EXPR );
			type++;
		}

		if ( type > 1 )
			handler
					.getErrorHandler( )
					.semanticError(
							new DesignParserException(
									DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		Module module = handler.getModule( );

		if ( image.getLocalProperty( module, ImageItem.SOURCE_PROP ) == null
				&& StringUtil.compareVersion( handler.getVersion( ), "3.2.3" ) < 1 )
			checkImageType( );

		String refType = image
				.getStringProperty( module, ImageItem.SOURCE_PROP );

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR
				.equalsIgnoreCase( refType ) )
		{
			String valueExpr = image.getStringProperty( module,
					ImageItem.VALUE_EXPR_PROP );
			if ( StringUtil.isEmpty( valueExpr ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE ) );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL
				.equalsIgnoreCase( refType )
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE
						.equalsIgnoreCase( refType ) )
		{
			String uri = image.getStringProperty( module, ImageItem.URI_PROP );
			if ( StringUtil.isEmpty( uri ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGE_URL_VALUE ) );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
				.equalsIgnoreCase( refType ) )
		{
			String name = image.getStringProperty( module,
					ImageItem.IMAGE_NAME_PROP );

			if ( StringUtil.isEmpty( name ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
										DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGE_NAME_VALUE ) );
			}
		}

		super.end( );
	}
}