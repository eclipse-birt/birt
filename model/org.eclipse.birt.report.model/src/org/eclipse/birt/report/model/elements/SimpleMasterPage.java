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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;

/**
 * This class represents a Simple Master Page element in the report design. Use
 * the {@link org.eclipse.birt.report.model.api.SimpleMasterPageHandle}class to
 * access the page-header and page-footer slot of the simple master page. The
 * simple master page provides a header and footer that appear on every page.
 * The page margins define the position of the content area on the page. The
 * page header and footer reside within the content area. The page header at the
 * top on each page, and the page footer at the bottom.
 * 
 * 
 */

public class SimpleMasterPage extends MasterPage implements ISimpleMasterPageModel {
	/**
	 * Default Constructor.
	 */

	public SimpleMasterPage() {
		super();
		initSlots();
	}

	/**
	 * Constructs the simple master page with a required name.
	 * 
	 * @param theName the required name of this master page.
	 */

	public SimpleMasterPage(String theName) {
		super(theName);
		initSlots();
	}

	/**
	 * Return the handle of this element.
	 * 
	 * @param module the report design
	 * @return the handle of this element
	 */

	public SimpleMasterPageHandle handle(Module module) {
		if (handle == null) {
			handle = new SimpleMasterPageHandle(module, this);
		}
		return (SimpleMasterPageHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert slot == PAGE_HEADER_SLOT || slot == PAGE_FOOTER_SLOT;
		return slots[slot];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.MasterPage#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitSimpleMasterPage(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}
}
