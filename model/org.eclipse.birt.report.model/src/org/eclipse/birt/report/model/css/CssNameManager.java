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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Theme;

/**
 * Manager css style
 *
 */

public class CssNameManager {

	/**
	 * Get all css style element with different name. If style elements has the same
	 * name , just get last style element.
	 *
	 * @param operation design element ,here is report design or theme.
	 * @return list each item is <code>CssStyle</code>
	 */

	public static List<CssStyle> getStyles(ICssStyleSheetOperation operation) {
		Map<String, CssStyle> stylesMap = new HashMap<>();
		List<CssStyle> styles = new ArrayList<>();

		List<CssStyleSheet> csses = operation.getCsses();
		for (int i = csses.size() - 1; i >= 0; --i) {
			CssStyleSheet sheet = csses.get(i);
			List<CssStyle> tmpstyles = sheet.getStyles();
			for (int j = 0; j < tmpstyles.size(); ++j) {
				CssStyle tmpStyle = tmpstyles.get(j);
				if (stylesMap.get(tmpStyle.getName()) == null) {
					stylesMap.put(tmpStyle.getName(), tmpStyle);
					styles.add(tmpStyle);
				}
			}
		}
		return styles;
	}

	/**
	 * Get css style through style name
	 *
	 * @param operation design element ,here is report design or theme.
	 * @param styleName
	 * @return css style element
	 */

	public static StyleElement getStyle(ICssStyleSheetOperation operation, String styleName) {
		assert styleName != null;

		List<CssStyleSheet> csses = operation.getCsses();
		for (int i = csses.size() - 1; i >= 0; --i) {
			CssStyleSheet sheet = csses.get(i);
			List<CssStyle> tmpstyles = sheet.getStyles();
			for (int j = 0; j < tmpstyles.size(); ++j) {
				CssStyle tmpStyle = tmpstyles.get(j);
				if (styleName.equalsIgnoreCase(tmpStyle.getName())) {
					return tmpStyle;
				}
			}
		}
		return null;
	}

	/**
	 * Unresloves style element in design element.
	 *
	 * @param css css style sheet list , each item is <code>CssStyle</code>.
	 */

	public static void adjustStylesForRemove(CssStyleSheet css) {
		List<CssStyle> styles = css.getStyles();
		Iterator<CssStyle> iter = styles.iterator();
		while (iter.hasNext()) {
			CssStyle style = iter.next();
			// unresolve styles itself first
			style.updateClientReferences();
		}
	}

	/**
	 * Resloves style element in design element.
	 * <tr>
	 * <td>First adjust styles in itself, if lower precedence , should unresolve
	 * style's back-reference.
	 * <td>Second if element is report design , should unresolve theme style's
	 * back-reference.
	 * </tr>
	 *
	 * @param module
	 * @param cssOperation
	 * @param sheet
	 * @param position
	 */

	public static void adjustStylesForAdd(Module module, ICssStyleSheetOperation cssOperation, CssStyleSheet sheet,
			int position) {
		// element is theme or report design.

		List<CssStyleSheet> csses = cssOperation.getCsses();

		// position decide precedence
		for (int i = 0; i < position; ++i) {
			CssStyleSheet tmpSheet = csses.get(i);
			List<CssStyle> tmpStyles = tmpSheet.getStyles();

			for (int j = 0; j < tmpStyles.size(); ++j) {
				CssStyle cssStyle = tmpStyles.get(j);
				if (sheet.findStyle(cssStyle.getName()) != null) {
					// unresolved all the client elements
					cssStyle.updateClientReferences();
				}
			}
		}

		// if element is report design , should unresolve style in theme.
		if (cssOperation instanceof ReportDesign) {
			Theme theme = module.getTheme();
			if (theme == null) {
				return;
			}

			cssOperation = theme;
			csses = cssOperation.getCsses();

			for (int i = 0; i < csses.size(); ++i) {
				CssStyleSheet tmpSheet = csses.get(i);
				List<CssStyle> tmpStyles = tmpSheet.getStyles();

				for (int j = 0; j < tmpStyles.size(); ++j) {
					CssStyle cssStyle = tmpStyles.get(j);
					if (sheet.findStyle(cssStyle.getName()) != null) {
						// unresolved all the client elements
						cssStyle.updateClientReferences();
					}
				}
			}
		}
	}

}
