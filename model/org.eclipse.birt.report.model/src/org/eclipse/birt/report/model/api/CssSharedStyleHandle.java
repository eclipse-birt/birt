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

import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;

/**
 * Css shared style handle.
 * 
 */

public class CssSharedStyleHandle extends SharedStyleHandle {

	private CssStyleSheet cssSheet;

	/**
	 * Constructor
	 * 
	 * @param module
	 * @param element
	 * 
	 */

	public CssSharedStyleHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Constructor
	 * 
	 * @param module
	 * @param element
	 * @param cssSheet
	 */

	public CssSharedStyleHandle(Module module, DesignElement element, CssStyleSheet cssSheet) {
		super(module, element);
		this.cssSheet = cssSheet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignElementHandle#getContainerSlotHandle
	 * ()
	 */

	public SlotHandle getContainerSlotHandle() {
		return null;
	}

	/**
	 * Gets css style sheet handle.
	 * 
	 * @return the css style sheet handle.
	 */

	public CssStyleSheetHandle getCssStyleSheetHandle() {
		if (cssSheet == null)
			return null;
		return cssSheet.handle(module);
	}

}
