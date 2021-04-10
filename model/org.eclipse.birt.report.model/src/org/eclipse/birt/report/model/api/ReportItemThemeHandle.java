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

import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemThemeModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.StyleUtil;

/**
 * Represents a report item theme in the library. Each theme contains some
 * number of styles.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.ReportItemTheme
 */

public class ReportItemThemeHandle extends AbstractThemeHandle implements IReportItemThemeModel {

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ReportItemThemeHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets the type for this report item theme. The type for this theme is
	 * required. If it is not set, no style can be inserted to this theme. The type
	 * can be the predefined choices as one of the following:
	 * <ul>
	 * <li>REPORT_ITEM_THEME_TYPE_TABLE
	 * <li>REPORT_ITEM_THEME_TYPE_LIST
	 * <li>REPORT_ITEM_THEME_TYPE_GRID
	 * </ul>
	 * At the another side, the type can be extension name for the extended item,
	 * such as crosstab and chart and others. Generally, the type is the name of the
	 * element definition{@link IElementDefn#getName()} .
	 * 
	 * @return
	 */
	public String getType() {
		return getStringProperty(TYPE_PROP);
	}

	/**
	 * Sets the type for this report item theme. The type for this theme is
	 * required. If it is not set, no style can be inserted to this theme. The type
	 * can be the predefined choices as one of the following:
	 * <ul>
	 * <li>REPORT_ITEM_THEME_TYPE_TABLE
	 * <li>REPORT_ITEM_THEME_TYPE_LIST
	 * <li>REPORT_ITEM_THEME_TYPE_GRID
	 * </ul>
	 * At the another side, the type can be extension name for the extended item,
	 * such as crosstab and chart and others. Generally, the type is the name of the
	 * element definition{@link IElementDefn#getName()} .
	 * 
	 * @param type the type to set for this report item theme
	 */
	public void setType(String type) throws SemanticException {
		setStringProperty(TYPE_PROP, type);
	}

	/**
	 * Imports the selected styles in a <code>CssStyleSheetHandle</code> to the
	 * given theme of the library. Each in the list is instance of
	 * <code>SharedStyleHandle</code> .If any style selected has a duplicate name
	 * with that of one style already existing in the report design, this method
	 * will rename it and then add it to the design.
	 * 
	 * @param stylesheet     the style sheet handle that contains all the selected
	 *                       styles
	 * @param selectedStyles the selected style list
	 * @param themeName      the name of the theme to put styles
	 */

	public void importCssStyles(CssStyleSheetHandle stylesheet, List<SharedStyleHandle> selectedStyles)
			throws SemanticException {
		ActivityStack stack = module.getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.IMPORT_CSS_STYLES_MESSAGE));

		for (int i = 0; i < selectedStyles.size(); i++) {
			SharedStyleHandle style = selectedStyles.get(i);

			try {
				// Copy CssStyle to Style

				SharedStyleHandle newStyle = StyleUtil.transferCssStyleToSharedStyle(module, style);

				if (newStyle == null)
					continue;

				getStyles().add(newStyle);
			} catch (SemanticException e) {
				stack.rollback();
				throw e;
			}
		}

		stack.commit();
	}

	/**
	 *
	 * @return
	 */
	public String getCustomValues() {
		return getStringProperty(CUSTOM_VALUES_PROP);
	}

	/**
	 *
	 * @param values
	 * @throws SemanticException
	 */
	public void setCustomValues(String values) throws SemanticException {
		setStringProperty(CUSTOM_VALUES_PROP, values);
	}
}
