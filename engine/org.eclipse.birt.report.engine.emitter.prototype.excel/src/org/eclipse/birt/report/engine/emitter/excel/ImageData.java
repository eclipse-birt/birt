/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.emitter.EmitterUtil;

public class ImageData extends SheetData
{

	private int rowNo, colNo;
	private double height, width;
	private String altText, imageUrl;
	private byte[] imageData;

	public ImageData( IImageContent image, StyleEntry style, int datatype,
			XlsContainer currentContainer )
	{
		super( );
		this.style = style;
		this.datatype = datatype;
		height = ExcelUtil.convertImageSize( image.getHeight( ), 0 );
		width = minWidth( currentContainer.getSizeInfo( ).getWidth( ), image
				.getWidth( ) );
		altText = image.getAltText( );
		imageUrl = image.getURI( );
		imageData = EmitterUtil.parseImage( image, image.getImageSource( ), image.getURI( ),image.getMIMEType( ) ,
				image.getExtension( ) );
		container = currentContainer;
		rowSpanInDesign = 0;
	}

	private double minWidth( int containerWidth, DimensionType imageWidth )
	{
		double imageWid = ExcelUtil.convertImageSize( imageWidth, 0 );
		if ( imageWid >= containerWidth )
			return containerWidth;
		return imageWid;

	}

	public double getHeight( )
	{
		return height;
	}

	public void setHeight( double height )
	{
		this.height = height;
	}

	public double getWidth( )
	{
		return width;
	}

	public void setWidth( double width )
	{
		this.width = width;
	}

	public String getDescription( )
	{
		return altText;
	}

	public void setDescription( String description )
	{
		this.altText = description;
	}

	public String getImageUrl( )
	{
		return imageUrl;
	}

	public void setUrl( String url )
	{
		this.imageUrl = url;
	}

	public byte[] getImageData( )
	{
		return imageData;
	}

	public void setImageData( byte[] imageData )
	{
		this.imageData = imageData;
	}

	public int getRowno( )
	{
		return rowNo;
	}

	public void setRowNo( int rowno )
	{
		this.rowNo = rowno;
	}

	public int getColNo( )
	{
		return colNo;
	}

	public void setColNo( int colno )
	{
		this.colNo = colno;
	}

}
