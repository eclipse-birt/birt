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
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.interfaces.IFreeFormModel;

/**
 * Represents a free-form element. Free-form is the simplest form of report
 * container. A container item holds a collection of other report items. Every
 * item in the container is positioned at an (x, y) location relative to the top
 * left corner of the container. In Free-form elements can be positioned
 * anywhere.
 */

public class FreeFormHandle extends ReportItemHandle implements IFreeFormModel {

	/**
	 * Constructs a free-form handle with the given design and the free-from. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public FreeFormHandle(Module module, FreeForm element) {
		super(module, element);
	}

	/**
	 * Returns a slot handle to work with the Report Items within the free-form.
	 * 
	 * @return a slot handle for the report items in the free-from.
	 * @see SlotHandle
	 */

	public SlotHandle getReportItems() {
		return getSlot(IFreeFormModel.REPORT_ITEMS_SLOT);
	}

	/**
	 * Increases the z-index of the given element by 1. If the element is not in the
	 * freeform, do nothing.
	 * 
	 * @param content the element
	 * @throws SemanticException
	 */

	public void bringForward(ReportItemHandle content) throws SemanticException {
		if (content == null)
			return;
		if (!content.getElement().isContentOf(getElement()))
			return;

		int zIndex = content.getZIndex();
		if (zIndex < getMaximalZIndex())
			content.setZIndex(zIndex + 1);
	}

	/**
	 * Reduces the z-index of the given element that resides in the freeform. If the
	 * element is not in the freeform, do nothing.
	 * 
	 * @param content the element
	 * @throws SemanticException
	 */

	public void sendBackward(ReportItemHandle content) throws SemanticException {
		if (content == null)
			return;
		if (!content.getElement().isContentOf(getElement()))
			return;

		int zIndex = content.getZIndex();
		if (zIndex == 0)
			return;
		content.setZIndex(zIndex - 1);
	}

	/**
	 * Increases the z-index of the given element so that the element will have the
	 * maximal z-index value. If the element is not in the freeform, do nothing.
	 * 
	 * @param content the element
	 * @throws SemanticException
	 */

	public void bringToFront(ReportItemHandle content) throws SemanticException {
		if (content == null)
			return;
		if (!content.getElement().isContentOf(getElement()))
			return;

		int zIndex = content.getZIndex();
		int maxZIndex = getMaximalZIndex();
		if (zIndex < maxZIndex)
			content.setZIndex(maxZIndex + 1);
	}

	/**
	 * Reduces the z-index of the given element so that the element will have the
	 * minimal z-index value. If the element is not in the freeform, do nothing.
	 * 
	 * @param content the element
	 * @throws SemanticException
	 */

	public void sendToBack(ReportItemHandle content) throws SemanticException {
		if (content == null)
			return;
		if (!content.getElement().isContentOf(getElement()))
			return;

		int zIndex = content.getZIndex();
		if (zIndex == 0)
			return;
		content.setZIndex(0);
	}

	/**
	 * Calculates the maximal z depth of the freeform. The maximal value is defined
	 * by the content whose z-index is maximum.
	 * 
	 * @return the maximal z-index
	 */

	private int getMaximalZIndex() {
		int maxZIndex = 0;

		SlotHandle slot = getSlot(IFreeFormModel.REPORT_ITEMS_SLOT);
		for (int i = 0; i < slot.getCount(); i++) {
			ReportItemHandle item = (ReportItemHandle) slot.get(i);
			int tmpZIndex = item.getZIndex();
			if (tmpZIndex > maxZIndex)
				maxZIndex = tmpZIndex;
		}

		return maxZIndex;
	}
}