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
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.URIUtil;
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
	 * Temporary variable to track the type of the image reference.
	 */

	private int imageRef = -1;

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

	public ImageState( DesignParserHandler handler, DesignElement theContainer,
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
		setProperty( ImageItem.SIZE_PROP, attrs,
				DesignSchemaConstants.IMAGE_SIZE_ATTRIB );
		setProperty( ImageItem.SCALE_PROP, attrs,
				DesignSchemaConstants.IMAGE_SCALE_ATTRIB );
		super.parseAttrs( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ALT_TEXT_TAG ) )
			return new ExternalTextState( handler, image,
					ImageItem.ALT_TEXT_PROP );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HELP_TEXT_TAG ) )
			return new ExternalTextState( handler, image,
					ImageItem.HELP_TEXT_PROP );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.ACTION_TAG ) )
			return new ActionState( handler, image, ImageItem.ACTION_PROP );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_REF_URI_TAG ) )
		{
			if ( imageRef != -1 )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
			}

			imageRef = 0;
			return new TextState( handler, image, ImageItem.URI_PROP );
		}
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.IMAGE_REF_IMAGENAME_TAG ) )
		{
			if ( imageRef != -1 )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
			}
			imageRef = 1;
			return new TextState( handler, image, ImageItem.IMAGE_NAME_PROP );
		}
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.IMAGE_REF_VALUE_EXPR_TAG ) )
		{
			if ( imageRef == 0 || imageRef == 1 )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
			}
			imageRef = 2;
			return new TextState( handler, image, ImageItem.VALUE_EXPR_PROP );
		}
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.IMAGE_REF_TYPE_EXPR_TAG ) )
		{
			if ( imageRef == 0 || imageRef == 1 )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
			}
			imageRef = 2;
			return new TextState( handler, image, ImageItem.TYPE_EXPR_PROP );
		}
		return super.startElement( tagName );
	}

	/**
	 * Check whether the source type conflicts, and set the proper source type.
	 */

	private void checkImageType( )
	{

		int type = 0;
		ReportDesign design = handler.getDesign( );

		String uri = (String) image.getLocalProperty( design,
				ImageItem.URI_PROP );
		if ( !StringUtil.isBlank( uri ) )
		{
			String filePath = URIUtil.getLocalPath( uri );
			if ( !StringUtil.isBlank( filePath ) )
			{
				setProperty( ImageItem.SOURCE_PROP,
						DesignChoiceConstants.IMAGE_REF_TYPE_FILE );
			}
			else
			{
				setProperty( ImageItem.SOURCE_PROP,
						DesignChoiceConstants.IMAGE_REF_TYPE_URL );
			}
			type++;
		}

		String name = (String) image.getLocalProperty( design,
				ImageItem.IMAGE_NAME_PROP );
		if ( !StringUtil.isBlank( name ) )
		{
			setProperty( ImageItem.SOURCE_PROP,
					DesignChoiceConstants.IMAGE_REF_TYPE_EMBED );
			type++;
		}

		String typeExpr = (String) image.getLocalProperty( design,
				ImageItem.TYPE_EXPR_PROP );
		String valueExpr = (String) image.getLocalProperty( design,
				ImageItem.VALUE_EXPR_PROP );

		if ( !StringUtil.isBlank( typeExpr ) || !StringUtil.isBlank( valueExpr ) )
		{
			setProperty( ImageItem.SOURCE_PROP,
					DesignChoiceConstants.IMAGE_REF_TYPE_EXPR );
			type++;
		}

		if ( type > 1 )
			handler.semanticError( new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end( ) throws SAXException
	{
		ReportDesign design = handler.getDesign( );

		checkImageType( );

		String refType = image
				.getStringProperty( design, ImageItem.SOURCE_PROP );

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR
				.equalsIgnoreCase( refType ) )
		{
			String typeExpr = image.getStringProperty( design,
					ImageItem.TYPE_EXPR_PROP );
			String valueExpr = image.getStringProperty( design,
					ImageItem.VALUE_EXPR_PROP );
			if ( StringUtil.isBlank( typeExpr )
					|| StringUtil.isBlank( valueExpr ) )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE ) );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL
				.equalsIgnoreCase( refType )
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE
						.equalsIgnoreCase( refType ) )
		{
			String uri = image.getStringProperty( design, ImageItem.URI_PROP );
			if ( StringUtil.isBlank( uri ) )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGE_URL_VALUE ) );
			}
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
				.equalsIgnoreCase( refType ) )
		{
			String name = image.getStringProperty( design,
					ImageItem.IMAGE_NAME_PROP );

			if ( StringUtil.isBlank( name ) )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.DESIGN_EXCEPTION_INVALID_IMAGE_NAME_VALUE ) );
			}
		}

		super.end( );
	}

}