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
	String getOrientation();

	/**
	 * @return Returns the pageType.
	 */
	String getPageType();

	/**
	 * @return Returns the pageHeight.
	 */
	DimensionType getPageHeight();

	/**
	 * @return Returns the pageWidth.
	 */
	DimensionType getPageWidth();

	/**
	 * @return Returns the waterMark.
	 */
	IImageContent getWaterMark();

	Collection getHeader();

	Collection getFooter();

	IContent getPageHeader();

	IContent getPageFooter();

	IContent getPageBody();

	void setPageHeader(IContent header);

	void setPageFooter(IContent footer);

	void setPageBody(IContent body);

	DimensionType getMarginTop();

	DimensionType getMarginBottom();

	DimensionType getMarginLeft();

	DimensionType getMarginRight();

	/**
	 * @return Returns the header height
	 */
	DimensionType getHeaderHeight();

	/**
	 * @return Returns the footer height
	 */
	DimensionType getFooterHeight();

	/**
	 * @return Returns the width of the left part
	 */
	DimensionType getLeftWidth();

	/**
	 * @return Returns the width of the right part
	 */
	DimensionType getRightWidth();

	/**
	 * @deprecated use getPageBody().getStyle()
	 * @return Returns the content style.
	 */
	@Deprecated
	IStyle getContentStyle();

	long getPageNumber();

	void setPageNumber(long pageNumber);

	/**
	 * @deprecated use getPageBody().getComputedStyle()
	 */
	@Deprecated
	IStyle getContentComputedStyle();

}
