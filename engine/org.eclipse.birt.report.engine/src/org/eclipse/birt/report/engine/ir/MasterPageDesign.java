/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

/**
 * Master Page
 * 
 */
abstract public class MasterPageDesign extends StyledElementDesign {

	/**
	 * Page Type, such as A4, USLetter, USLegal
	 */
	protected String pageType;
	/**
	 * Page Width, must be a absoluted dimension
	 */
	protected DimensionType pageWidth;
	/**
	 * Page Height, must be a absoluted dimension
	 */
	protected DimensionType pageHeight;
	/**
	 * left margin, must be a absoluted dimension
	 */
	protected DimensionType leftMargin;
	/**
	 * right margin, must be a absoluted dimension
	 */
	protected DimensionType rightMargin;
	/**
	 * bottom margin, must be a absoluted dimension
	 */
	protected DimensionType bottomMargin;
	/**
	 * top margin, must be a absoluted dimension
	 */
	protected DimensionType topMargin;

	/**
	 * page orientation, one of the
	 */
	protected String orientation;

	protected String bodyStyleName;

	private Expression onPageStart;
	private Expression onPageEnd;

	/**
	 * default constuctor use A4, 5mm margin.
	 */
	public MasterPageDesign() {
	}

	/**
	 * set the marign.
	 * 
	 * margin must be described in absoluted units.
	 * 
	 * @param top    top margin
	 * @param left   left margin
	 * @param bottom bottom margin
	 * @param right  right margin
	 */
	public void setMargin(DimensionType top, DimensionType left, DimensionType bottom, DimensionType right) {
		this.topMargin = top;
		this.leftMargin = left;
		this.bottomMargin = bottom;
		this.rightMargin = right;
	}

	/**
	 * get left margin
	 * 
	 * @return left margin(mm)
	 */
	public DimensionType getLeftMargin() {
		return this.leftMargin;
	}

	/**
	 * get rigth margin
	 * 
	 * @return right margin(mm)
	 */
	public DimensionType getRightMargin() {
		return this.rightMargin;
	}

	/**
	 * get top margin
	 * 
	 * @return top margin in mm
	 */
	public DimensionType getTopMargin() {
		return this.topMargin;
	}

	/**
	 * get bottom margin in mm
	 * 
	 * @return bottom margin
	 */
	public DimensionType getBottomMargin() {
		return this.bottomMargin;
	}

	/**
	 * get the page type. page type can be A4 or CUSTOM.
	 * 
	 * @return page type.
	 */
	public String getPageType() {
		return this.pageType;
	}

	/**
	 * set the page type. if page type is not custom type, set the width&height to
	 * the corresponse value.
	 * 
	 * @param pageType
	 */
	public void setPageType(String pageType) {
		this.pageType = pageType;
		return;
	}

	/**
	 * set the page size
	 * 
	 * @param width  width of the page(mm)
	 * @param height height of the page(mm)
	 */
	public void setPageSize(DimensionType width, DimensionType height) {
		this.pageWidth = width;
		this.pageHeight = height;
	}

	/**
	 * get page width
	 * 
	 * @return page width in mm
	 */
	public DimensionType getPageWidth() {
		return this.pageWidth;
	}

	/**
	 * get page height
	 * 
	 * @return page height in mm
	 */
	public DimensionType getPageHeight() {
		return this.pageHeight;
	}

	/**
	 * @return Returns the orientation.
	 */
	public String getOrientation() {
		return orientation;
	}

	/**
	 * @param orientation The orientation to set.
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getBodyStyleName() {
		return bodyStyleName;
	}

	public void setBodyStyleName(String bodyStyleName) {
		this.bodyStyleName = bodyStyleName;
	}

	public Expression getOnPageStart() {
		return onPageStart;
	}

	public void setOnPageStart(Expression onPageStart) {
		this.onPageStart = onPageStart;
	}

	public Expression getOnPageEnd() {
		return onPageEnd;
	}

	public void setOnPageEnd(Expression onPageEnd) {
		this.onPageEnd = onPageEnd;
	}
}
