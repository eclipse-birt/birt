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

/**
 * Page instance.
 * 
 * The page may contains multiple columns, which is defined in the MasterPage in
 * report design.
 * 
 * The content is defined by getBody().
 * 
 * @version $Revision: 1.5 $ $Date: 2005/11/02 10:36:13 $
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
	public String getPageHeight( );

	/**
	 * @return Returns the pageWidth.
	 */
	public String getPageWidth( );
	
	/**
	 * @return Returns the waterMark.
	 */
	public IImageContent getWaterMark( );
	
	public List getHeader();
	
	public List getFooter();
	
	public String getMarginTop();
	
	public String getMarginBottom();
	
	public String getMarginLeft();
	
	public String getMarginRight();
	
	/**
	 * @return Returns the header height
	 */
	public String getHeaderHeight( );

	/**
	 * @return Returns the footer height
	 */
	public String getFooterHeight( );

	/**
	 * @return Returns the width of the left part
	 */
	public String getLeftWidth( );

	/**
	 * @return Returns the width of the right part
	 */
	public String getRightWidth( );

	/**
	 * @return Returns the showFloatingFooter.
	 */
	public boolean isShowFloatingFooter( );

	/**
	 * @return Returns the showFooterOnLast.
	 */
	public boolean isShowFooterOnLast( );

	/**
	 * @return Returns the showHeaderOnFirst.
	 */
	public boolean isShowHeaderOnFirst( );
	
	/**
	 * @return Returns the content style.
	 */
	public IStyle getContentStyle( );
	
	public long getPageNumber();
	
	void setPageNumber(long pageNumber);
	
}