/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.content;

import java.util.Collection;

import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * Page instance.
 * 
 * The page may contains multiple columns, which is defined in the MasterPage in
 * report design.
 * 
 * The content is defined by getBody().
 * 
 */
public interface IPageContent extends IContainerContent {

	/**
	 * @return Returns the orientation.
	 */
	public String getOrientation();

	/**
	 * @return Returns the pageType.
	 */
	public String getPageType();

	/**
	 * @return Returns the pageHeight.
	 */
	public DimensionType getPageHeight();

	/**
	 * @return Returns the pageWidth.
	 */
	public DimensionType getPageWidth();

	/**
	 * @return Returns the waterMark.
	 */
	public IImageContent getWaterMark();

	public Collection getHeader();

	public Collection getFooter();

	public IContent getPageHeader();

	public IContent getPageFooter();

	public IContent getPageBody();

	public void setPageHeader(IContent header);

	public void setPageFooter(IContent footer);

	public void setPageBody(IContent body);

	public DimensionType getMarginTop();

	public DimensionType getMarginBottom();

	public DimensionType getMarginLeft();

	public DimensionType getMarginRight();

	/**
	 * @return Returns the header height
	 */
	public DimensionType getHeaderHeight();

	/**
	 * @return Returns the footer height
	 */
	public DimensionType getFooterHeight();

	/**
	 * @return Returns the width of the left part
	 */
	public DimensionType getLeftWidth();

	/**
	 * @return Returns the width of the right part
	 */
	public DimensionType getRightWidth();

	/**
	 * @deprecated use getPageBody().getStyle()
	 * @return Returns the content style.
	 */
	public IStyle getContentStyle();

	public long getPageNumber();

	void setPageNumber(long pageNumber);

	/**
	 * @deprecated use getPageBody().getComputedStyle()
	 */
	public IStyle getContentComputedStyle();

}
