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
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemThemeModel;
import org.eclipse.birt.report.model.elements.strategy.FlattenCopyPolicy;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * This class represents a theme in the library.
 *
 */

public class ReportItemTheme extends AbstractTheme implements IReportItemThemeModel {

	/**
	 * Constructor.
	 */

	public ReportItemTheme() {
		super();
	}

	/**
	 * Constructor with the element name.
	 *
	 * @param theName the element name
	 */

	public ReportItemTheme(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitReportItemTheme(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.REPORT_ITEM_THEME_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design of the row
	 *
	 * @return an API handle for this element
	 */

	public ReportItemThemeHandle handle(Module module) {
		if (handle == null) {
			handle = new ReportItemThemeHandle(module, this);
		}
		return (ReportItemThemeHandle) handle;
	}

	public static boolean isValidType(String type) {
		if (StringUtil.isBlank(type)) {
			return false;
		}
		List<IPredefinedStyle> styles = MetaDataDictionary.getInstance().getPredefinedStyles(type);
		if (styles == null || styles.isEmpty()) {
			return false;
		}
		return true;
	}

	public String getType(Module module) {
		return getStringProperty(module, TYPE_PROP);
	}

	public Object FlattenClone() throws CloneNotSupportedException {
		return doClone(FlattenCopyPolicy.getInstance());
	}
}
