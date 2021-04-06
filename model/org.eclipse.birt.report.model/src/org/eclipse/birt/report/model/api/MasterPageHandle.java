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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;

/**
 * Represents a master page. The master page is an abstract element that defines
 * the basic properties of a printed page. The derived elements, Simple and
 * Graphic Master Pages, provide content that appears on the page itself.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.MasterPage
 * @see DimensionHandle
 */

public abstract class MasterPageHandle extends ReportElementHandle implements IMasterPageModel {

	/**
	 * Constructs a master-page handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public MasterPageHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the size of the page. The size is either one of the standard sizes,
	 * or a custom size. Note that the size returned <em>will not</em> match the
	 * <code>getWidth</code> and <code>getHeight</code> values unless the page uses
	 * a custom size.
	 * 
	 * @return the actual page size in application units
	 */

	public Point getSize() {
		return ((MasterPage) element).getSize(module);
	}

	/**
	 * Returns the the effective width of the page.
	 * 
	 * @return the effective width of the page. Return value is a DimensionValue,
	 *         the measure of it is the width measure of the page, unit is that set
	 *         on the session.
	 * @deprecated
	 */

	public DimensionValue getEffectiveWidth() {
		return new DimensionValue(getSize().x, module.getSession().getUnits());
	}

	/**
	 * Returns the the effective height of the page.
	 * 
	 * @return the effective height of the page. Return value is a DimensionValue,
	 *         the measure of it is the height measure of the page, unit is that set
	 *         on the session.
	 * @deprecated
	 */

	public DimensionValue getEffectiveHeight() {
		return new DimensionValue(getSize().y, module.getSession().getUnits());
	}

	/**
	 * Returns the type of the page. The return type of the page is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_SIZE_CUSTOM</code>
	 * <li><code>PAGE_SIZE_US_LETTER</code>
	 * <li><code>PAGE_SIZE_US_LEGAL</code>
	 * <li><code>PAGE_SIZE_A4</code>
	 * </ul>
	 * 
	 * @return the type of the page
	 */

	public String getPageType() {
		return getStringProperty(IMasterPageModel.TYPE_PROP);
	}

	/**
	 * Sets the type of the page. The input type of the page is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_SIZE_CUSTOM</code>
	 * <li><code>PAGE_SIZE_US_LETTER</code>
	 * <li><code>PAGE_SIZE_US_LEGAL</code>
	 * <li><code>PAGE_SIZE_A4</code>
	 * </ul>
	 * 
	 * @param type the type of the page
	 * 
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 */

	public void setPageType(String type) throws SemanticException {
		setStringProperty(IMasterPageModel.TYPE_PROP, type);
	}

	/**
	 * Returns the page orientation. The return type of the page is defined in
	 * <code>DesignChoiceConstants</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_ORIENTATION_AUTO</code>
	 * <li><code>PAGE_ORIENTATION_PORTRAIT</code>
	 * <li><code>PAGE_ORIENTATION_LANDSCAPE</code>
	 * </ul>
	 * 
	 * @return the page orientation
	 */

	public String getOrientation() {
		return getStringProperty(IMasterPageModel.ORIENTATION_PROP);
	}

	/**
	 * Returns the page orientation. The input type of the page is defined in
	 * <code>DesignChoiceConstants</code> can be one of:
	 * 
	 * <ul>
	 * <li><code>PAGE_ORIENTATION_AUTO</code>
	 * <li><code>PAGE_ORIENTATION_PORTRAIT</code>
	 * <li><code>PAGE_ORIENTATION_LANDSCAPE</code>
	 * </ul>
	 * 
	 * @param orientation the page orientation
	 * @throws SemanticException if the property is locked or the input value is not
	 *                           one of the above.
	 */

	public void setOrientation(String orientation) throws SemanticException {
		setStringProperty(IMasterPageModel.ORIENTATION_PROP, orientation);
	}

	/**
	 * Gets a dimension handle to work with the height of the page.
	 * 
	 * @return a DimensionHandle to work with the height
	 */

	public DimensionHandle getHeight() {
		return super.getDimensionProperty(IMasterPageModel.HEIGHT_PROP);
	}

	/**
	 * Gets a dimension handle to work with the width of the page.
	 * 
	 * @return DimensionHandle to work with the width
	 */

	public DimensionHandle getWidth() {
		return super.getDimensionProperty(IMasterPageModel.WIDTH_PROP);
	}

	/**
	 * Gets a dimension handle to work with the margin on the bottom side.
	 * 
	 * @return a DimensionHandle for the bottom margin.
	 */

	public DimensionHandle getBottomMargin() {
		return super.getDimensionProperty(IMasterPageModel.BOTTOM_MARGIN_PROP);
	}

	/**
	 * Gets a dimension handle to work with the margin on the left side.
	 * 
	 * @return a DimensionHandle for the left margin.
	 */

	public DimensionHandle getLeftMargin() {
		return super.getDimensionProperty(IMasterPageModel.LEFT_MARGIN_PROP);
	}

	/**
	 * Gets a dimension handle to work with the margin on the right side.
	 * 
	 * @return a DimensionHandle for the right margin.
	 */

	public DimensionHandle getRightMargin() {
		return super.getDimensionProperty(IMasterPageModel.RIGHT_MARGIN_PROP);
	}

	/**
	 * Gets a dimension handle to work with the margin on the top side.
	 * 
	 * @return a DimensionHandle for the top margin.
	 */

	public DimensionHandle getTopMargin() {
		return super.getDimensionProperty(IMasterPageModel.TOP_MARGIN_PROP);
	}

	/**
	 * Gets the effective page height.
	 * 
	 * @return the page height
	 */

	public DimensionValue getPageHeight() {
		return (DimensionValue) getProperty(IMasterPageModel.HEIGHT_PROP);
	}

	/**
	 * Gets the effective page width.
	 * 
	 * @return the page width
	 */

	public DimensionValue getPageWidth() {
		return (DimensionValue) getProperty(IMasterPageModel.WIDTH_PROP);
	}

	/**
	 * Gets the script of onPageStart method.
	 * 
	 * @return the script of onPageStart method.
	 */
	public String getOnPageStart() {
		return getStringProperty(ON_PAGE_START_METHOD);
	}

	/**
	 * Sets the script of onPageStart method.
	 * 
	 * @param onPageStart the script of onPageStart method.
	 * @throws SemanticException if the property is locked by masks.
	 */
	public void setOnPageStart(String onPageStart) throws SemanticException {
		setStringProperty(ON_PAGE_START_METHOD, onPageStart);
	}

	/**
	 * Gets the script of onPageEnd method.
	 * 
	 * @return the script of onPageEnd method.
	 */
	public String getOnPageEnd() {
		return getStringProperty(ON_PAGE_END_METHOD);
	}

	/**
	 * Sets the script of onPageEnd method.
	 * 
	 * @param onPageEnd the script of onPageEnd method.
	 * @throws SemanticException if the property is locked by masks.
	 */
	public void setOnPageEnd(String onPageEnd) throws SemanticException {
		setStringProperty(ON_PAGE_END_METHOD, onPageEnd);
	}

	/**
	 * Returns the number of columns in the report.
	 * 
	 * @return the number of columns in the report
	 */

	public int getColumnCount() {
		return getIntProperty(COLUMNS_PROP);
	}

	/**
	 * Sets the number of columns in the report.
	 * 
	 * @param count the number of columns in the report
	 * @throws SemanticException if the property is locked.
	 */

	public void setColumnCount(int count) throws SemanticException {
		setIntProperty(COLUMNS_PROP, count);
	}

	/**
	 * Returns a handle to work with the the space between columns.
	 * 
	 * @return a DimensionHandle to deal with the space between columns.
	 */

	public DimensionHandle getColumnSpacing() {
		return super.getDimensionProperty(COLUMN_SPACING_PROP);
	}
}