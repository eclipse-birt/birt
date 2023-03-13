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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.MasterPageMultiColumnValidator;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IGraphicMaterPageModel;

/**
 * This class represents a Graphic Master Page element in the report design. A
 * graphic master page describes a physical page free-form page 'decoration'.
 * The name of the master page is required and must be unique within the design.
 * The decoration can include simple headers and footers, but can also include
 * content within the left and right margins, as well as watermarks under the
 * content area. The page can contain multiple columns. In a multi-column
 * report, the content area is the area inside the margins defined by each
 * column. Note that each page has only one content area, though that content
 * area can be divided into multiple columns. That is, a page has one content
 * area. If a page has multiple columns, the column layout is 'overlayed' on top
 * of the content area. Use the
 * {@link org.eclipse.birt.report.model.api.GraphicMasterPageHandle}class to
 * access the content slot of the graphic master page.
 *
 */
public class GraphicMasterPage extends MasterPage implements IGraphicMaterPageModel {

	/**
	 * Default Constructor.
	 */

	public GraphicMasterPage() {
		super();
		initSlots();
	}

	/**
	 * Constructs the graphic master page with a required and unique name.
	 *
	 * @param theName the required name
	 */

	public GraphicMasterPage(String theName) {
		super(theName);
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	@Override
	public ContainerSlot getSlot(int slot) {
		assert slot == CONTENT_SLOT;
		return slots[CONTENT_SLOT];
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public GraphicMasterPageHandle handle(Module module) {
		if (handle == null) {
			handle = new GraphicMasterPageHandle(module, this);
		}
		return (GraphicMasterPageHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.MasterPage#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitGraphicMasterPage(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		List<SemanticException> pageSizeErrors = MasterPageTypeValidator.getInstance().validate(module, this);
		if (pageSizeErrors.isEmpty()) {
			list.addAll(MasterPageMultiColumnValidator.getInstance().validate(module, this));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}
}
