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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IGraphicMaterPageModel;

/**
 * Represents a graphic master page in the design. A graphic master page
 * describes a physical page "decoration". The decoration can include simple
 * headers and footers, but can also include content within the left and right
 * margins, as well as watermarks under the content area. The page can contain
 * multiple columns. In a multi-column report, the content area is the area
 * inside the margins defined by each column.
 * <p>
 * Note that each page has only one content area, though that content area can
 * be divided into multiple columns. That is, a page has one content area. If a
 * page has multiple columns, the column layout is overlayed on top of the
 * content area.
 */

public class GraphicMasterPageHandle extends MasterPageHandle implements IGraphicMaterPageModel {

	/**
	 * Constructs a handle with the given design and the design element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public GraphicMasterPageHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the slot handle for the content. The items in this slot appear on the
	 * page itself, usually as headers, footers, margins, watermarks, etc.
	 * 
	 * @return a handle to the content slot
	 * @see SlotHandle
	 */

	public SlotHandle getContent() {
		return getSlot(CONTENT_SLOT);
	}
}
