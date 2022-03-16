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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;

/**
 * Represents a simple master page. The simple master page provides a header and
 * footer that appear on every page.
 *
 */

public class SimpleMasterPageHandle extends MasterPageHandle implements ISimpleMasterPageModel {

	/**
	 * Constructs the handle for a simple master page with the given design and
	 * element. The application generally does not create handles directly. Instead,
	 * it uses one of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public SimpleMasterPageHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Tests whether to show the page header on the first page of the report.
	 *
	 * @return <code>true</code> if allows to show the header on the first page.
	 */

	public boolean showHeaderOnFirst() {
		return getBooleanProperty(ISimpleMasterPageModel.SHOW_HEADER_ON_FIRST_PROP);
	}

	/**
	 * Changes the status to show page header on the first page or not.
	 *
	 * @param showHeaderOnFirst <code>true</code> if allow show header on the first
	 *                          page, <code>false</code> otherwise.
	 * @throws SemanticException if the property is locked.
	 */

	public void setShowHeaderOnFirst(boolean showHeaderOnFirst) throws SemanticException {
		setBooleanProperty(ISimpleMasterPageModel.SHOW_HEADER_ON_FIRST_PROP, showHeaderOnFirst);
	}

	/**
	 * Tests whether to show the page footer on the last page.
	 *
	 * @return <code>true</code> if allows to show the footer on the last page.
	 */

	public boolean showFooterOnLast() {
		return getBooleanProperty(ISimpleMasterPageModel.SHOW_FOOTER_ON_LAST_PROP);
	}

	/**
	 * Changes the status to show footer on the last page.
	 *
	 * @param showFooterOnLast <code>true</code> to allow to show footer on last
	 *                         page, <code>false</code> otherwise.
	 * @throws SemanticException if the property is locked.
	 */

	public void setShowFooterOnLast(boolean showFooterOnLast) throws SemanticException {
		setBooleanProperty(ISimpleMasterPageModel.SHOW_FOOTER_ON_LAST_PROP, showFooterOnLast);
	}

	/**
	 * Tests whether allows the footer 'floats' after the last content on each page.
	 *
	 * @return <code>true</code> if the simple master page allows floating footer.
	 */

	public boolean isFloatingFooter() {
		return getBooleanProperty(ISimpleMasterPageModel.FLOATING_FOOTER);
	}

	/**
	 * Changes the status to say if it has a floating footer or not.
	 *
	 * @param isFloatingFooter <code>true</code> to allow the footer floating,
	 *                         <code>false</code> not.
	 * @throws SemanticException if the property is locked.
	 */

	public void setFloatingFooter(boolean isFloatingFooter) throws SemanticException {
		setBooleanProperty(ISimpleMasterPageModel.FLOATING_FOOTER, isFloatingFooter);
	}

	/**
	 * Returns the page header slot of this simple master page.
	 *
	 * @return the page header slot handle.
	 */

	public SlotHandle getPageHeader() {
		return getSlot(ISimpleMasterPageModel.PAGE_HEADER_SLOT);
	}

	/**
	 * Returns the page footer slot of this simple master page.
	 *
	 * @return the page footer slot handle.
	 */

	public SlotHandle getPageFooter() {
		return getSlot(ISimpleMasterPageModel.PAGE_FOOTER_SLOT);
	}

	/**
	 * Gets a dimension handle to work with the height on page header.
	 *
	 * @return a DimensionHandle for the header height.
	 */

	public DimensionHandle getHeaderHeight() {
		return super.getDimensionProperty(ISimpleMasterPageModel.HEADER_HEIGHT_PROP);
	}

	/**
	 * Gets a dimension handle to work with the height on page footer.
	 *
	 * @return a DimensionHandle for the header footer.
	 */

	public DimensionHandle getFooterHeight() {
		return super.getDimensionProperty(ISimpleMasterPageModel.FOOTER_HEIGHT_PROP);
	}
}
