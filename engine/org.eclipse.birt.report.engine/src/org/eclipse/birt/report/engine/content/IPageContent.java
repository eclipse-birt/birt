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

package org.eclipse.birt.report.engine.content;

import java.util.List;

import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * Page instance.
 * 
 * The page may contains multiple columns, which is defined in the MasterPage in
 * report design.
 * 
 * The content is defined by getBody().
 * 
 * @version $Revision: 1.1 $ $Date: 2005/11/11 06:26:46 $
 */
public interface IPageContent extends IContent
{

	/**
	 * @return Returns the orientation.
	 */
	public String getOrientation( );

	/**
	 * @return Returns the pageType.
	 */
	public String getPageType( );

	/**
	 * @return Returns the pageHeight.
	 */
	public DimensionType getPageHeight( );

	/**
	 * @return Returns the pageWidth.
	 */
	public DimensionType getPageWidth( );

	/**
	 * @return Returns the waterMark.
	 */
	public IImageContent getWaterMark( );

	public List getHeader( );

	public List getFooter( );

	public DimensionType getMarginTop( );

	public DimensionType getMarginBottom( );

	public DimensionType getMarginLeft( );

	public DimensionType getMarginRight( );

	/**
	 * @return Returns the header height
	 */
	public DimensionType getHeaderHeight( );

	/**
	 * @return Returns the footer height
	 */
	public DimensionType getFooterHeight( );

	/**
	 * @return Returns the width of the left part
	 */
	public DimensionType getLeftWidth( );

	/**
	 * @return Returns the width of the right part
	 */
	public DimensionType getRightWidth( );

	/**
	 * @return Returns the content style.
	 */
	public IStyle getContentStyle( );

	public long getPageNumber( );

	void setPageNumber( long pageNumber );

}