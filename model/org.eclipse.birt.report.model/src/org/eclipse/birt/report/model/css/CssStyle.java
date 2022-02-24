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

package org.eclipse.birt.report.model.css;

import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElementAdapter;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;

/**
 * This class represents a shared css style which can't be modified.
 * 
 */

public class CssStyle extends Style {

	private CssStyleSheet sheet;

	/**
	 * Set css style container.
	 * 
	 * @param obj
	 */

	protected void setContainer(DesignElement obj) {
		if (obj instanceof ReportDesign) {
			super.setContainer(obj, IReportDesignModel.CSSES_PROP);
		} else if (obj instanceof Theme) {
			super.setContainer(obj, IAbstractThemeModel.CSSES_PROP);
		}
	}

	/**
	 * Default constructor.
	 */

	public CssStyle() {
	}

	/**
	 * Constructs the css style element with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public CssStyle(String theName) {
		super(theName);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design of the style
	 * 
	 * @return an API handle for this element
	 */

	public SharedStyleHandle handle(Module module) {
		if (handle == null) {
			handle = new CssSharedStyleHandle(module, this, sheet);
		}
		return (SharedStyleHandle) handle;
	}

	/**
	 * Gets css style sheet.
	 * 
	 * @return css style sheet.
	 */

	public CssStyleSheet getCssStyleSheet() {
		return sheet;
	}

	/**
	 * Set css style sheet.
	 * 
	 * @param sheet
	 */

	public void setCssStyleSheet(CssStyleSheet sheet) {
		this.sheet = sheet;
		setContainer(sheet.getContainer());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#canDrop(org.eclipse.birt.
	 * report.model.core.Module)
	 */

	public boolean canDrop(Module module) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#canEdit(org.eclipse.birt.
	 * report.model.core.Module)
	 */

	public boolean canEdit(Module module) {
		return false;
	}

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		CssStyle newStyle = (CssStyle) super.doClone(policy);
		ReferenceableElementAdapter newAdapter = new ReferenceableElementAdapter(newStyle);
		newAdapter.clearClients();
		newStyle.adapter = newAdapter;
		return newStyle;
	}
}
