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
	 * Get the orientation
	 *
	 * @return the orientation
	 */
	String getOrientation();

	/**
	 * Get the page type
	 *
	 * @return the page type
	 */
	String getPageType();

	/**
	 * Get the page height
	 *
	 * @return the page height
	 */
	DimensionType getPageHeight();

	/**
	 * Get the page width
	 *
	 * @return the page width
	 */
	DimensionType getPageWidth();

	/**
	 * Get the watermark
	 *
	 * @return the watermark
	 */
	IImageContent getWaterMark();

	/**
	 * Get the header
	 *
	 * @return the header
	 */
	Collection<IContent> getHeader();

	/**
	 * Get the footer
	 *
	 * @return the footer
	 */
	Collection<IContent> getFooter();

	/**
	 * Get the page header
	 *
	 * @return the page header
	 */
	IContent getPageHeader();

	/**
	 * Get the page footer
	 *
	 * @return the page footer
	 */
	IContent getPageFooter();

	/**
	 * Get the page body
	 *
	 * @return the page body
	 */
	IContent getPageBody();

	/**
	 * Set the page header
	 *
	 * @param header header content
	 */
	void setPageHeader(IContent header);

	/**
	 * Set the page footer
	 *
	 * @param footer footer content
	 */
	void setPageFooter(IContent footer);

	/**
	 * Set the pagebody
	 *
	 * @param body body content
	 */
	void setPageBody(IContent body);

	/**
	 * Get the margin top
	 *
	 * @return the margin top
	 */
	DimensionType getMarginTop();

	/**
	 * Get the margin bottom
	 *
	 * @return the margin bottom
	 */
	DimensionType getMarginBottom();

	/**
	 * Get the margin left
	 *
	 * @return the margin left
	 */
	DimensionType getMarginLeft();

	/**
	 * Get the margin right
	 *
	 * @return the margin right
	 */
	DimensionType getMarginRight();

	/**
	 * Get the header height
	 *
	 * @return the header height
	 */
	DimensionType getHeaderHeight();

	/**
	 * Get the footer height
	 *
	 * @return the footer height
	 */
	DimensionType getFooterHeight();

	/**
	 * Get the width of the left part
	 *
	 * @return the width of the left part
	 */
	DimensionType getLeftWidth();

	/**
	 * Get the width of the right part
	 *
	 * @return the width of the right part
	 */
	DimensionType getRightWidth();

	/**
	 * Get the content style
	 *
	 * @return the content style
	 * @deprecated use getPageBody().getStyle()
	 */
	@Deprecated
	IStyle getContentStyle();

	/**
	 * Get the page number
	 *
	 * @return page number
	 */
	long getPageNumber();

	/**
	 * Set page number
	 *
	 * @param pageNumber page number
	 */
	void setPageNumber(long pageNumber);

	/**
	 * Get the computed style of content
	 *
	 * @return computed style of content
	 * @deprecated use getPageBody().getComputedStyle()
	 */
	@Deprecated
	IStyle getContentComputedStyle();

}
